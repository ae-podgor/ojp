package ru.otus.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.model.Banknote;

import java.util.List;

public class AtmImpl implements Atm {
    private static final Logger logger = LoggerFactory.getLogger(AtmImpl.class);

    private final AtmService atmService;

    public AtmImpl(AtmService atmService) {
        this.atmService = atmService;
    }

    @Override
    public void put(List<Banknote> banknotes) {
        logger.info("Putting banknotes into ATM '{}'", banknotes);
        atmService.put(banknotes);
    }

    @Override
    public List<Banknote> take(Integer amount) {
        logger.info("Taking cash amount '{}' from ATM", amount);
        List<Banknote> taken = atmService.take(amount);
        logger.info("Taken banknotes from ATM '{}'", taken);
        return taken;
    }

    @Override
    public Integer getBalance() {
        logger.info("Getting balance from the ATM");
        Integer balance = atmService.getBalance();
        logger.info("Balance from the ATM '{}'", balance);
        return balance;
    }
}
