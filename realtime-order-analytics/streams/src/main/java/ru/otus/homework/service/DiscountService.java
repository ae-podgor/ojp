package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.NavigableMap;
import java.util.TreeMap;

import static ru.otus.homework.config.KafkaStreamsConfig.TOTAL_ORDERS_BY_USER_STORE;

/**
 * Пример использования данных общего количества заказов.
 * В учебных целях все объединено в один сервис и сильно упрощено
 */
@Service
@RequiredArgsConstructor
public class DiscountService {

    private final NavigableMap<Long, Long> discountTable = new TreeMap<>();
    private final KafkaStreams totalOrdersKafkaStreams;

    {
        // от 0 заказов — 0%, от 5 — 5%, от 10 — 10%, от 20 — 20%
        discountTable.put(0L, 0L);
        discountTable.put(5L, 5L);
        discountTable.put(10L, 10L);
        discountTable.put(20L, 20L);
    }

    public long getDiscountPercentByOrders(long ordersCount) {
        Long key = discountTable.floorKey(ordersCount);
        if (key == null) {
            return 0;
        }
        return discountTable.get(key);
    }

    public long getTotalOrdersForUser(String userId) {
        if (userId == null) {
            return 0L;
        }
        try {
            ReadOnlyKeyValueStore<String, Long> store = totalOrdersKafkaStreams.store(
                    StoreQueryParameters.fromNameAndType(TOTAL_ORDERS_BY_USER_STORE,
                            QueryableStoreTypes.keyValueStore()));
            Long val = store.get(userId);
            return val == null ? 0L : val;
        } catch (Exception e) {
            return 0L;
        }
    }
}