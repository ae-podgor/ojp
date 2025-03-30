package ru.otus.homework.service;

import ru.otus.homework.annotation.After;
import ru.otus.homework.annotation.Before;
import ru.otus.homework.annotation.Test;

public class TestingClass {

    @Before
    public static void initTest() {
        System.out.println("I am Before test");
//        throw new RuntimeException();
    }

    @After
    public static void finishTest() {
        System.out.println("I am After test");
    }

    @Test
    public void test1() {
        System.out.println("I am Test 1");
    }

    @Test
    public void test2() {
        System.out.println("I am Test 2");
    }

    @Test
    public void test3() {
        System.out.println("I am Test 3, throwing an exception");
        throw new RuntimeException();
    }

}
