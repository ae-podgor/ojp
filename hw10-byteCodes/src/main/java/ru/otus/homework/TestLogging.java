package ru.otus.homework;

import ru.otus.homework.annotation.Log;

public class TestLogging implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
    }

    @Override
    public void calculation(int param1, int param2) {
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
    }
}
