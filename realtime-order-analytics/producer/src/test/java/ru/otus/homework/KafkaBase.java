package ru.otus.homework;

import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.kafka.KafkaContainer;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;

class KafkaBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaBase.class);
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer("apache/kafka-native:3.9.1");
    @Getter
    private static String bootstrapServers;

    public static void start(Collection<NewTopic> topics) throws ExecutionException, InterruptedException,
            TimeoutException {
        KAFKA_CONTAINER.start();
        bootstrapServers = KAFKA_CONTAINER.getBootstrapServers();

        LOGGER.info("topics creation...");
        try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers))) {
            var result = admin.createTopics(topics);

            for (var topicResult : result.values().values()) {
                topicResult.get(10, TimeUnit.SECONDS);
            }
        }
        LOGGER.info("topics created");
    }
}
