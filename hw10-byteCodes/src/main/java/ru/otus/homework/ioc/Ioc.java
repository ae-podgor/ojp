package ru.otus.homework.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> interfaceClass, T impl) {
        InvocationHandler handler = new DemoInvocationHandler<>(impl);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{interfaceClass}, handler);
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T target;
        private final Map<Method, Boolean> logAnnotationCache = new HashMap<>();

        public DemoInvocationHandler(T target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (!logAnnotationCache.containsKey(method)) {
                logAnnotationCache.put(method, checkIfLogAnnotationPresent(method));
            }

            if (logAnnotationCache.getOrDefault(method, false)) {
                String params = args != null ? Arrays.toString(args) : "none";
                logger.info("invoking method: {}, params: {}", method.getName(), params);
            }
            return method.invoke(target, args);
        }

        private boolean checkIfLogAnnotationPresent(Method interfaceMethod) {
            try {
                Method implementationMethod = target.getClass()
                        .getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
                return implementationMethod.isAnnotationPresent(Log.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Method not found in implementation: " + interfaceMethod, e);
            }
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "target=" + target + '}';
        }
    }
}
