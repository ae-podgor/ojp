package ru.otus.homework;

import com.google.common.collect.ImmutableList;

public class HelloOtus {
    public static void main(String[] args) {
        ImmutableList<String> immutableList = ImmutableList.of("one", "two", "three");

        immutableList.forEach(System.out::println);
    }
}