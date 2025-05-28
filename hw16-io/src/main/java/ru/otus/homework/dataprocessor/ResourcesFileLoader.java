package ru.otus.homework.dataprocessor;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.homework.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = Objects.requireNonNull(fileName);
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
            return MAPPER.readValue(resourceAsStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new FileProcessException("Error reading resource: %s".formatted(fileName), e);
        }
    }
}
