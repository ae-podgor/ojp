package ru.otus.homework.api;

import ru.otus.homework.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
