package ru.otus.homework.service;

import ru.otus.homework.annotation.After;
import ru.otus.homework.annotation.Before;
import ru.otus.homework.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {

    public static void runTests(Class<?> c) throws ReflectiveOperationException {
        System.out.println("Going to run tests in class: '%s'".formatted(TestingClass.class.getSimpleName()));
        List<Method> methods = List.of(c.getDeclaredMethods());
        // Доп. проверка, что методы в классе есть
        if (methods.isEmpty()) {
            System.out.printf("No methods in class '%s'".formatted(c.getSimpleName()));
            return;
        }
        List<Method> beforeMethods = methods.stream()
                .filter(method -> method.isAnnotationPresent(Before.class)).toList();
        List<Method> afterMethods = methods.stream()
                .filter(method -> method.isAnnotationPresent(After.class)).toList();
        List<Method> testMethods = methods.stream()
                .filter(method -> method.isAnnotationPresent(Test.class)).toList();
        Map<String, Boolean> testResults = new HashMap<>();
        for (Method testMethod : testMethods) {
            Object newInstance = c.getConstructor().newInstance();
            boolean testSuccess = true;
            try {
                if (!beforeMethods.isEmpty()) {
                    runMethods(newInstance, beforeMethods);
                }
                invokeTest(newInstance, testMethod);
            } catch (Exception e) {
                testSuccess = false;
            } finally {
                try {
                    if (!afterMethods.isEmpty()) {
                        runMethods(newInstance, afterMethods);
                    }
                } catch (Exception e) {
                    testSuccess = false;
                }
            }
            testResults.put(testMethod.getName(), testSuccess);
        }

        printResult(testResults);
    }

    private static void runMethods(Object instance, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(instance);
        }
    }

    private static void invokeTest(Object instance, Method testMethod) throws InvocationTargetException, IllegalAccessException {
        testMethod.invoke(instance);
    }

    private static void printResult(Map<String, Boolean> testResults) {
        System.out.println("--------------------------");
        System.out.println("Test Results:");
        testResults.forEach((methodName, result) -> System.out.printf("Test '%s' : %s%n", methodName, result ? "SUCCESS" : "FAILED"));
        System.out.println("--------------------------");
        System.out.println("Tests total: %d".formatted(testResults.size()));
        System.out.println("Successful tests: %d".formatted(testResults.values().stream().filter(result -> result).count()));
        System.out.println("Failed tests: %d".formatted(testResults.values().stream().filter(result -> !result).count()));
    }

}
