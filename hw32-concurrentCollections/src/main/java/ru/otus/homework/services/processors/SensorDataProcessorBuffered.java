package ru.otus.homework.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.api.SensorDataProcessor;
import ru.otus.homework.api.model.SensorData;
import ru.otus.homework.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {

    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final List<SensorData> dataBuffer = new CopyOnWriteArrayList<>();
    private final Object lock = new Object();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        synchronized (lock) {
            dataBuffer.add(data);
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        }
    }

    public void flush() {
        List<SensorData> bufferedData;
        synchronized (lock) {
            if (!dataBuffer.isEmpty()) {
                bufferedData = new ArrayList<>(dataBuffer);
                dataBuffer.clear();
            } else {
                return;
            }
        }

        bufferedData.sort(Comparator.comparing(SensorData::getMeasurementTime));
        try {
            writer.writeBufferedData(bufferedData);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
