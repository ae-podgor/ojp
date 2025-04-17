package ru.otus.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.data.Storage;
import ru.otus.homework.exception.AtmException;
import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AtmServiceImpl implements AtmService {
    private static final Logger logger = LoggerFactory.getLogger(AtmServiceImpl.class);

    private final Storage storage;

    public AtmServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void put(List<Banknote> banknotes) {
        if (logger.isDebugEnabled()) {
            Integer sum = banknotes.stream()
                    .map(Banknote::nominal)
                    .map(Denomination::getValue)
                    .reduce(Integer::sum)
                    .orElse(0);
            logger.debug("Put sum: '{}'", sum);
        }
        storage.updateCash(banknotes);
    }

    @Override
    public List<Banknote> take(Integer amount) {
        if (amount % 100 != 0) {
            throw new AtmException("Введите сумму кратную 100");
        }
        Integer balance = getBalance();
        if (balance == 0 || balance < amount) {
            throw new AtmException("Недостаточно денег в банкомате");
        }
        List<Banknote> byMinBanknotes = getByMinBanknotes(amount);
        storage.removeCash(byMinBanknotes);
        return byMinBanknotes;
    }

    @Override
    public Integer getBalance() {
        Map<Denomination, Integer> allStoredMoney = storage.getCash();
        return allStoredMoney.entrySet().stream()
                .map(stored -> stored.getKey().getValue() * stored.getValue())
                .reduce(Integer::sum).orElse(0);
    }

    private List<Banknote> getByMinBanknotes(Integer amount) {
        logger.debug("getByMinBanknotes: amount '{}'", amount);
        Map<Denomination, Integer> availableMoney = storage.getCash();
        List<Denomination> denominations = Arrays.asList(Denomination.values());
        Collections.reverse(denominations);

        int remaining = amount;
        List<Banknote> result = new ArrayList<>();

        for (Denomination nominal : denominations) {
            int nominalValue = nominal.getValue();
            int availableCountByNominal = availableMoney.getOrDefault(nominal, 0);
            if (availableCountByNominal == 0) continue;

            int count = Math.min(remaining / nominalValue, availableCountByNominal);
            for (int i = 0; i < count; i++) {
                result.add(new Banknote(nominal));
            }
            remaining -= count * nominalValue;

            if (remaining == 0) break;
        }

        if (remaining != 0) {
            throw new AtmException("Невозможно выдать сумму заданными банкнотами");
        }

        return result;
    }
}
