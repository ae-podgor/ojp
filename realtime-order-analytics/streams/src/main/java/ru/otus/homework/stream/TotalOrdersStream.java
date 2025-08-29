package ru.otus.homework.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.ContextualProcessor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Order;

import static ru.otus.homework.config.KafkaStreamsConfig.TOTAL_ORDERS_BY_USER_STORE;

/**
 * Собирает статистику суммарного количества заказов клиента
 */
@Slf4j
@Service
public class TotalOrdersStream extends ContextualProcessor<String, Order, String, Long> {

    private KeyValueStore<String, Long> store;

    @Override
    public void init(ProcessorContext<String, Long> context) {
        super.init(context);
        this.store = context.getStateStore(TOTAL_ORDERS_BY_USER_STORE);
        log.info("Initialized TotalOrdersService with store '{}'", TOTAL_ORDERS_BY_USER_STORE);
        // можно делать снэпшоты стора
//        context.schedule(Duration.ofSeconds(30), PunctuationType.WALL_CLOCK_TIME,
//                timestamp -> {
//                    try (KeyValueIterator<String, Long> it = store.all()) {
//                        while (it.hasNext()) {
//                            var kv = it.next();
//                            log.info("store snapshot key={} total={}", kv.key, kv.value);
//                        }
//                    } catch (Exception e) {
//                        log.error("Error while iterating store snapshot", e);
//                    }
//                });
    }

    @Override
    public void process(Record<String, Order> record) {
        if (record == null || record.value() == null) {
            log.warn("Received null record or null value, skipping");
            return;
        }

        String userId = record.key();
        Order order = record.value();
        Long current = store.get(userId);
        if (current == null) current = 0L;

        current = switch (order.status()) {
            case ACCEPTED -> current + 1;
            case CANCELLED -> Math.max(0L, current - 1); // только в учебном проекте
        };

        store.put(userId, current);
        log.debug("Updated total orders amount for user '{}' -> '{}'", userId, current);
    }

}