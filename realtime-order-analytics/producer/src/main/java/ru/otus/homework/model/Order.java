package ru.otus.homework.model;

import java.time.LocalDateTime;

public record Order(String orderId, String userId, LocalDateTime eventTime, OrderStatus status) {}
