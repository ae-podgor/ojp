package ru.otus.homework.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageImpl implements Storage {
    private static final Logger logger = LoggerFactory.getLogger(StorageImpl.class);

    private final Map<Denomination, Integer> cashMap;

    {
        logger.debug("Initializing cash map");
        cashMap = new HashMap<>();
        cashMap.put(Denomination.RUB_100, 0);
        cashMap.put(Denomination.RUB_200, 0);
        cashMap.put(Denomination.RUB_500, 0);
        cashMap.put(Denomination.RUB_1000, 0);
        cashMap.put(Denomination.RUB_2000, 0);
        cashMap.put(Denomination.RUB_5000, 0);
    }

    @Override
    public void updateCash(List<Banknote> banknotes) {
        for (Banknote m : banknotes) {
            cashMap.compute(m.nominal(), (k, v) -> v == 0 ? 1 : v + 1);
        }
    }

    @Override
    public Map<Denomination, Integer> getCash() {
        return new HashMap<>(cashMap);
    }

    @Override
    public void removeCash(List<Banknote> banknotes) {
        for (Banknote m : banknotes) {
            cashMap.compute(m.nominal(), (k, v) -> v == 0 ? 0 : v - 1);
        }
    }
}
