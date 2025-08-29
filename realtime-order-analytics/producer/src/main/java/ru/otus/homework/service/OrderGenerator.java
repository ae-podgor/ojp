package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Order;
import ru.otus.homework.model.OrderStatus;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderGenerator {

    private static final Random RND = new Random();
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    private final ProducerService producerService;

    @PostConstruct
    public void start() {
        EXECUTOR.scheduleAtFixedRate(this::generateAndPublish, 0, 100, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stop() {
        EXECUTOR.shutdownNow();
    }

    private void generateAndPublish() {
        try {
            Order order = generateRandom();
            producerService.send(order);
            log.debug("Generated random order id '{}' for user '{}'", order.orderId(), order.userId());
        } catch (Exception e) {
            log.error("Error generating order", e);
        }
    }

    private Order generateRandom() {
        String orderId = UUID.randomUUID().toString();
        String userId = "user-" + (1 + RND.nextInt(5));
        LocalDateTime eventTime = LocalDateTime.now();
        OrderStatus status = RND.nextDouble() < 0.9 ? OrderStatus.ACCEPTED : OrderStatus.CANCELLED;
        return new Order(orderId, userId, eventTime, status);
    }
}