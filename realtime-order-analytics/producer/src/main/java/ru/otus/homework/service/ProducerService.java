package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Order;

import jakarta.annotation.PreDestroy;

import static ru.otus.homework.config.KafkaProducerConfig.ORDERS_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {

    private final Producer<String, Order> kafkaProducer;

    public void send(Order order) {
        ProducerRecord<String, Order> record = new ProducerRecord<>(ORDERS_TOPIC, order.userId(), order);
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send order id '{}'", order.orderId(), exception);
            } else {
                log.info("Sent order id '{}' for user '{}': partition '{}' offset '{}'",
                        order.orderId(), order.userId(), metadata.partition(), metadata.offset());
            }
        });
    }

    @PreDestroy
    public void close() {
        try {
            kafkaProducer.flush();
            kafkaProducer.close();
        } catch (Exception e) {
            log.warn("Error closing producer", e);
        }
    }
}