package ru.otus.homework.config;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.config.properties.StreamsProperties;
import ru.otus.homework.model.Order;
import ru.otus.homework.stream.TotalOrdersStream;

import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.REPLICATION_FACTOR_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.STATE_DIR_CONFIG;

@Configuration
public class KafkaStreamsConfig {

    public static final String TOTAL_ORDERS_BY_USER_STORE = "total-orders-by-user-store";
    public static final String TOTAL_ORDERS_APP_ID = "total-orders-app";
    public static final String TOTAL_ORDERS_STATE_DIR = "/Users/apodgornova/git/ojp/realtime-order-analytics/streams/kafka-streams/".concat(TOTAL_ORDERS_APP_ID);
    public static final String USER_ACTIVITY_STORE = "user-activity-store";
    public static final String USER_ACTIVITY_APP_ID = "user-activity-app";
    public static final String USER_ACTIVITY_STATE_DIR = "/Users/apodgornova/git/ojp/realtime-order-analytics/streams/kafka-streams/".concat(USER_ACTIVITY_APP_ID);


    @Bean
    @ConfigurationProperties(prefix = StreamsProperties.APPLICATION_PROPERTIES)
    public StreamsProperties streamsApplicationProperties() {
        return new StreamsProperties();
    }

    @Bean
    public Serde<Order> orderJsonSerde() {
        JsonDeserializer<Order> deserializer = new JsonDeserializer<>(Order.class);
        JsonSerializer<Order> serializer = new JsonSerializer<>();
        return Serdes.serdeFrom(serializer, deserializer);
    }

    @Bean
    public Topology totalOrdersTopology(Serde<Order> orderJsonSerde, StreamsProperties streamsApplicationProperties) {
        StreamsBuilder builder = new StreamsBuilder();
        StoreBuilder<KeyValueStore<String, Long>> storeBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(TOTAL_ORDERS_BY_USER_STORE), Serdes.String(), Serdes.Long());

        builder.addStateStore(storeBuilder);
        builder.stream(streamsApplicationProperties.getInputTopic(), Consumed.with(Serdes.String(), orderJsonSerde))
                .process(TotalOrdersStream::new, TOTAL_ORDERS_BY_USER_STORE);

        return builder.build();
    }

    @Bean
    public KafkaStreams totalOrdersKafkaStreams(Topology totalOrdersTopology,
                                                StreamsProperties streamsApplicationProperties) {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, streamsApplicationProperties.getBootstrapServers());
        // consumer group ID
        props.put(APPLICATION_ID_CONFIG, TOTAL_ORDERS_APP_ID);
        // дефолтный serde для ключа
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        //путь на локальном диске, где Kafka Streams хранит локальные state stores
        props.put(STATE_DIR_CONFIG, TOTAL_ORDERS_STATE_DIR);
        // фактор репликации, для продакшна 2-3
        props.put(REPLICATION_FACTOR_CONFIG, 1);
        // интервал коммитов, по умолчанию раз в 30 сек
        props.put(COMMIT_INTERVAL_MS_CONFIG, 30000L);

        KafkaStreams streams = new KafkaStreams(totalOrdersTopology, props);
        streams.cleanUp(); // только для учебного проекта
        streams.start();

        return streams;
    }

}