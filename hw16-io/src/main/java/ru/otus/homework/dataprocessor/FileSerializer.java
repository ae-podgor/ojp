package ru.otus.homework.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class FileSerializer implements Serializer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = Objects.requireNonNull(fileName);
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        File file = new File(fileName);
        try {
            MAPPER.writeValue(file, data);
        } catch (IOException e) {
            throw new FileProcessException("Error while serializing file with name %s".formatted(fileName), e);
        }
    }
}
