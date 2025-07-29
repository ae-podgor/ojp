package ru.otus.homework.api;

import ru.otus.homework.api.model.SensorData;

public interface SensorDataProcessor {
    void process(SensorData data);

    default void onProcessingEnd() {}
}
