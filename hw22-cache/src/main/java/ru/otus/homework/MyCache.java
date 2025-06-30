package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> map = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();
    // Надо реализовать эти методы

    @Override
    public void put(K key, V value) {
        map.put(key, value);
        notifyListeners("put", key, value);
    }

    @Override
    public void remove(K key) {
        if (!map.containsKey(key)) return;
        V v = map.remove(key);
        notifyListeners("remove", key, v);
    }

    @Override
    public V get(K key) {
        V v = map.get(key);
        notifyListeners("get", key, v);
        return v;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String action, K key, V value) {
        if (listeners.isEmpty()) return;
        for (HwListener<K, V> listener : listeners) {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                logger.error("Error notifying listener: {}", e.getMessage());
                throw new RuntimeException("Error notifying listener: " + e.getMessage(), e);
            }
        }
    }
}
