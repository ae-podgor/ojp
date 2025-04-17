package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.data.Storage;
import ru.otus.homework.data.StorageImpl;
import ru.otus.homework.model.Banknote;
import ru.otus.homework.service.Atm;
import ru.otus.homework.service.AtmImpl;
import ru.otus.homework.service.AtmService;
import ru.otus.homework.service.AtmServiceImpl;

import java.util.List;

import static ru.otus.homework.model.Denomination.RUB_100;
import static ru.otus.homework.model.Denomination.RUB_1000;
import static ru.otus.homework.model.Denomination.RUB_200;
import static ru.otus.homework.model.Denomination.RUB_2000;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        logger.info("Starting to work");
        Storage storage = new StorageImpl();
        AtmService atmService = new AtmServiceImpl(storage);
        Atm atm = new AtmImpl(atmService);

        atm.put(List.of(
                new Banknote(RUB_100),
                new Banknote(RUB_100),
                new Banknote(RUB_100),
                new Banknote(RUB_100),
                new Banknote(RUB_200),
                new Banknote(RUB_1000)
        ));
        Integer balance = atm.getBalance();
        List<Banknote> taken = atm.take(300);
        Integer balance2 = atm.getBalance();
        atm.put(List.of(new Banknote(RUB_2000)));
        Integer balance3 = atm.getBalance();
    }

}
