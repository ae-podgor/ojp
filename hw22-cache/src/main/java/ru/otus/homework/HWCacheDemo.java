package ru.otus.homework;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.core.repository.DataTemplateHibernate;
import ru.otus.homework.core.sessionmanager.HibernateConfigManager;
import ru.otus.homework.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.service.DbServiceClientImpl;

import java.time.Duration;
import java.time.LocalDateTime;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
//        new HWCacheDemo().demo();
        new HWCacheDemo().demoHWCache();
    }

    private void demoHWCache() {
        TransactionManagerHibernate transactionManager = new HibernateConfigManager(new Configuration()).getTransactionManager();
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var cache = new MyCache<String, Client>();
        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        cache.addListener(listener);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, cache);

        LocalDateTime start = LocalDateTime.now();
        logger.info("Starting HW Cache Demo at {}", start);
        for (int i = 0; i < 5000; i++) {
            dbServiceClient.saveClient(new Client("client" + (i + 1)));
        }
//        logger.info("Cache size {}", cache.getSize());

        for (int i = 0; i < 5000; i++) {
            dbServiceClient.getClient(i + 1);
        }
        LocalDateTime end = LocalDateTime.now();
        logger.info("Finished HW Cache Demo at {}", end);
//        System.gc();
//        logger.info("Cache size {}", cache.getSize());
        logger.info("Total time: {} ms", Duration.between(start, end).toMillis());
//      Total time: 14383 ms
//      Total time without cache: 21371 ms
        cache.removeListener(listener);
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        @SuppressWarnings("java:S1604")
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
    }
}
