package ru.otus.homework.data;

import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.List;
import java.util.Map;

public interface Storage {

    void updateCash(List<Banknote> banknotes);
    Map<Denomination, Integer> getCash();
    void removeCash(List<Banknote> banknotes);

}
