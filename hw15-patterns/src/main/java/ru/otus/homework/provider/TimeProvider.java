package ru.otus.homework.provider;

import java.time.LocalDateTime;

public interface TimeProvider {

    LocalDateTime getDateTime();
}
