package ru.otus.homework.dataprocessor;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.homework.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
        URL resource = ResourcesFileLoader.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new FileProcessException("Resource not found: %s".formatted(fileName));
        }
        ArrayList<Measurement> measurements = new ArrayList<>();
        URI uri;
        try {
            uri = resource.toURI();
            File file = new File(uri);
            MAPPER.readTree(file).elements().forEachRemaining(element -> {
                Measurement measurement = new Measurement(element.get("name").asText(), element.get("value").asDouble());
                measurements.add(measurement);
            });
        } catch (URISyntaxException e) {
            throw new FileProcessException("Invalid URI syntax for resource: %s".formatted(fileName), e);
        } catch (IOException e) {
            throw new FileProcessException("Error reading resource: %s".formatted(fileName), e);
        }
        return measurements;
    }
}
