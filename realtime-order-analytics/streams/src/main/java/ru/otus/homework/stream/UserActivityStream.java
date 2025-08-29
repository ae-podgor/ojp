package ru.otus.homework.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.properties.StreamsProperties;
import ru.otus.homework.model.Order;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.REPLICATION_FACTOR_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.STATE_DIR_CONFIG;
import static ru.otus.homework.config.KafkaStreamsConfig.USER_ACTIVITY_APP_ID;
import static ru.otus.homework.config.KafkaStreamsConfig.USER_ACTIVITY_STATE_DIR;
import static ru.otus.homework.config.KafkaStreamsConfig.USER_ACTIVITY_STORE;

/**
 * Собирает статистику активности клиента - количество заказов (и отмен заказов) по userId каждые n секунд.
 */
@Slf4j
@Service
public class UserActivityStream {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault());

    private final KafkaStreams streams;

    public UserActivityStream(Serde<Order> orderJsonSerde, StreamsProperties streamsProperties) {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, Order> source = builder.stream(streamsProperties.getInputTopic(),
                Consumed.with(null, orderJsonSerde));

        source.selectKey((key, order) -> order.userId())
                .groupByKey(Grouped.with(null, orderJsonSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(streamsProperties.getWindowSec())))
                .count(Materialized.as(USER_ACTIVITY_STORE))
                .toStream()
                .foreach(UserActivityStream::logInfo);

        Properties props = getProperties(streamsProperties);

        streams = new KafkaStreams(builder.build(), props);
    }

    @PostConstruct
    public void start() {
        streams.cleanUp(); // только для учебного проекта
        streams.start();
    }

    @PreDestroy
    public void close() {
        streams.close();
    }

    private static Properties getProperties(StreamsProperties streamsProperties) {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, streamsProperties.getBootstrapServers());
        props.put(APPLICATION_ID_CONFIG, USER_ACTIVITY_APP_ID);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(STATE_DIR_CONFIG, USER_ACTIVITY_STATE_DIR);
        props.put(REPLICATION_FACTOR_CONFIG, 1);
        props.put(COMMIT_INTERVAL_MS_CONFIG, 5000L);
        return props;
    }

    private static void logInfo(Windowed<String> windowedUser, Long count) {
        String userId = windowedUser.key();
        long windowStartMillis = windowedUser.window().start();
        long windowEndMillis = windowedUser.window().end();

        String windowStart = fmt.format(Instant.ofEpochMilli(windowStartMillis));
        String windowEnd = fmt.format(Instant.ofEpochMilli(windowEndMillis));

        log.info("User '{}' orders count in window [{} - {}]: '{}'", userId, windowStart, windowEnd, count);
    }
}
