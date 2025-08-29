package ru.otus.homework;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.homework.model.Order;
import ru.otus.homework.model.OrderStatus;
import ru.otus.homework.service.ProducerService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.config.KafkaProducerConfig.ORDERS_TOPIC;

class ProducerServiceIntegrationTest {

    @BeforeAll
    static void setupKafka() throws Exception {
        KafkaBase.start(List.of(new NewTopic(ORDERS_TOPIC, 1, (short) 1)));
    }

    @Test
    void send_shouldProduceMessageToKafkaAndBeConsumed() {
        try (KafkaProducer<String, Order> realProducer = new KafkaProducer<>(getProducerProps())) {
            Order order = new Order("order-1", "user-1", LocalDateTime.now(), OrderStatus.ACCEPTED);

            ProducerService producerService = new ProducerService(realProducer);
            producerService.send(order);
            realProducer.flush();

            try (JsonDeserializer<Order> deserializer = new JsonDeserializer<>(Order.class);
                 KafkaConsumer<String, Order> consumer = new KafkaConsumer<>(getConsumerProps(),
                         new StringDeserializer(), deserializer)) {
                consumer.subscribe(List.of(ORDERS_TOPIC));

                boolean found = false;
                long deadline = System.currentTimeMillis() + 10_000;
                while (System.currentTimeMillis() < deadline && !found) {
                    ConsumerRecords<String, Order> records = consumer.poll(Duration.ofMillis(500));
                    if (!records.isEmpty()) {
                        records.forEach(r -> {
                            assertEquals(order.orderId(), r.value().orderId());
                            assertEquals(order.userId(), r.key());
                            assertEquals(order.status(), r.value().status());
                        });
                        found = true;
                    }
                }
                assertTrue(found, "Message was not consumed within timeout");
            }
        }
    }

    private static Map<String, Object> getProducerProps() {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, KafkaBase.getBootstrapServers(),
                KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
                VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName(),
                ACKS_CONFIG, "all"
        );
    }

    private static Properties getConsumerProps() {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBase.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + System.currentTimeMillis());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        return consumerProps;
    }
}

