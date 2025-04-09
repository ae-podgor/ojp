package ru.otus.homework.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.TestLogging;
import ru.otus.homework.TestLoggingInterface;
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

    public static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final Map<Method, Boolean> logAnnotationCache = new HashMap<>();

        public DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
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
            return method.invoke(myClass, args);
        }

        private boolean checkIfLogAnnotationPresent(Method interfaceMethod) {
            try {
                Method implementationMethod = myClass.getClass()
                        .getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
                return implementationMethod.isAnnotationPresent(Log.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Method not found in implementation: " + interfaceMethod, e);
            }
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
