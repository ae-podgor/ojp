package ru.otus.homework;

import ru.otus.homework.ioc.Ioc;

public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createMyClass(TestLoggingInterface.class, new TestLogging());
        myClass.calculation(8);
        myClass.calculation(100, 43);
        myClass.calculation(1, 2, "Hi");
    }
}