package ru.otus.homework.service;

import ru.otus.homework.model.Banknote;

import java.util.List;

public interface AtmService {

    void put(List<Banknote> banknotes);
    List<Banknote> take(Integer amount);
    Integer getBalance();

}
