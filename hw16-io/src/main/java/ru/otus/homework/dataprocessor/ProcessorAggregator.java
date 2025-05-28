package ru.otus.homework.dataprocessor;


import ru.otus.homework.model.Measurement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        return data.stream()
                .collect(Collectors.groupingBy(
                        Measurement::name, LinkedHashMap::new, Collectors.summingDouble(Measurement::value)));
    }
}
