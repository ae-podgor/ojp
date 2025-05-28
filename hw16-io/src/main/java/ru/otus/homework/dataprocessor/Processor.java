package ru.otus.homework.dataprocessor;


import ru.otus.homework.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
