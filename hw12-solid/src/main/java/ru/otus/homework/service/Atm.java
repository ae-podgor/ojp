package ru.otus.homework.service;

import ru.otus.homework.model.Banknote;

import java.util.List;

public interface Atm {

    void put(List<Banknote> banknotes);
    List<Banknote> take(Integer amount);
    Integer getBalance();
}
