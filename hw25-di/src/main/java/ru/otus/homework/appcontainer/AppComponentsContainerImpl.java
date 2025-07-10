package ru.otus.homework.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.homework.appcontainer.api.AppComponent;
import ru.otus.homework.appcontainer.api.AppComponentsContainer;
import ru.otus.homework.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        checkConfigClass(initialConfigClass);
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        Arrays.stream(initialConfigClass).forEach(this::checkConfigClass);
        sortConfigClasses(initialConfigClass);
        Arrays.stream(initialConfigClass).forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String packageName) {
        Class<?>[] configArray = getConfigClasses(packageName);
        sortConfigClasses(configArray);
        Arrays.stream(configArray).forEach(this::processConfig);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> collected = appComponents.stream()
                .filter(componentClass::isInstance)
                .map(componentClass::cast)
                .toList();
        if (collected.isEmpty()) {
            throw new IllegalArgumentException(
                    "Компонент с именем '%s' не найден.".formatted(componentClass.getName()));
        } else if (collected.size() > 1) {
            throw new IllegalArgumentException(
                    "Компонентов с именем '%s' больше одного.".formatted(componentClass.getName()));
        }
        return collected.getFirst();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);

        if (component == null) {
            throw new IllegalArgumentException("Компонент с именем '%s' не найден.".formatted(componentName));
        }
        return (C) component;
    }

    private Class<?>[] getConfigClasses(String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        if (configClasses.isEmpty()) {
            throw new IllegalArgumentException(
                    "В пакете '%s' нет классов с аннотацией @AppComponentsContainerConfig".formatted(packageName));
        }
        return configClasses.toArray(new Class<?>[0]);
    }

    private void sortConfigClasses(Class<?>... configClasses) {
        Arrays.sort(configClasses, Comparator.comparingInt(value ->
                value.getAnnotation(AppComponentsContainerConfig.class).order()));
    }

    private void processConfig(Class<?> configClass) {
        Method[] methods = configClass.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()));

        try {
            Object configInstance = configClass.getDeclaredConstructor().newInstance();

            for (Method method : methods) {
                if (method.isAnnotationPresent(AppComponent.class)) {
                    AppComponent annotation = method.getAnnotation(AppComponent.class);
                    String componentName = annotation.name();
                    if (appComponentsByName.containsKey(componentName)) {
                        throw new RuntimeException("Компонент с именем '%s' уже существует".formatted(componentName));
                    }

                    Object[] methodArguments = Arrays.stream(method.getParameterTypes())
                            .map(this::getAppComponent)
                            .toArray();

                    Object component = method.invoke(configInstance, methodArguments);
                    appComponents.add(component);
                    appComponentsByName.put(componentName, component);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing config class", e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format(
                    "Переданный класс не является конфигурацией '%s'", configClass.getName()));
        }
    }
}
