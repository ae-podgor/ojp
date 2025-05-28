package ru.otus.homework.dataprocessor;


import ru.otus.homework.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
