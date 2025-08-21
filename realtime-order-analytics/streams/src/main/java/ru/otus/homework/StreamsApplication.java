package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ru.otus.homework.config.properties.StreamsProperties;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = StreamsProperties.class)
public class StreamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamsApplication.class, args);
    }
}
