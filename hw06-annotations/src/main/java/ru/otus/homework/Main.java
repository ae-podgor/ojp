package ru.otus.homework;

import ru.otus.homework.service.TestRunner;
import ru.otus.homework.service.TestingClass;

public class Main {
    public static void main(String[] args) {
        try {
            TestRunner.runTests(TestingClass.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}