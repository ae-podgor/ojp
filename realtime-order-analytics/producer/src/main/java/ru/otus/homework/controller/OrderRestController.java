package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.model.Order;
import ru.otus.homework.model.OrderStatus;
import ru.otus.homework.service.ProducerService;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Для ручного создания клиента
 */
@RestController
@RequiredArgsConstructor
public class OrderRestController {

    private final ProducerService producerService;

    @PostMapping("/custom")
    public OrderRequest getDiscount(@RequestBody OrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        LocalDateTime eventTime = LocalDateTime.now();
        Order order = new Order(request.orderId, request.userId, eventTime, request.status);
        producerService.send(order);
        return request;
    }

    public record OrderRequest(String orderId, String userId, OrderStatus status) {}
}
