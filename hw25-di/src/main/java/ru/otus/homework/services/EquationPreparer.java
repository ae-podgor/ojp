package ru.otus.homework.services;

import ru.otus.homework.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
