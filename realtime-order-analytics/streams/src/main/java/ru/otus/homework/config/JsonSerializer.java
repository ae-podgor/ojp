package ru.otus.homework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            if (data == null) return null;
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Json serialization error", e);
        }
    }

}
