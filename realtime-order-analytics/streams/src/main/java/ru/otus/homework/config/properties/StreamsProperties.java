package ru.otus.homework.config.properties;

import lombok.Data;

@Data
public class StreamsProperties {

    public static final String APPLICATION_PROPERTIES = "streams";

    private String bootstrapServers;
    private String inputTopic;
    private Integer windowSec;

}