package ru.otus.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.data.Storage;
import ru.otus.homework.exception.AtmException;
import ru.otus.homework.model.Banknote;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.otus.homework.model.Denomination.RUB_1000;
import static ru.otus.homework.model.Denomination.RUB_500;

class AtmServiceImplTest {
    private Storage storage;
    private AtmServiceImpl atmService;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        atmService = new AtmServiceImpl(storage);
    }

    @Test
    @DisplayName("Корректное добавление банкнот в банкомат")
    void putCorrectBanknotes() {
        List<Banknote> banknotes = Arrays.asList(
                new Banknote(RUB_500),
                new Banknote(RUB_1000)
        );

        atmService.put(banknotes);

        verify(storage, times(1)).updateCash(banknotes);
    }

    @Test
    @DisplayName("Корректное снятие денег")
    void takeSuccess() {
        when(storage.getCash()).thenReturn(
                Map.of(RUB_1000, 2, RUB_500, 1)
        );
        List<Banknote> expected = Arrays.asList(
                new Banknote(RUB_1000),
                new Banknote(RUB_1000)
        );

        List<Banknote> actual = atmService.take(2000);

        assertEquals(expected, actual);
        verify(storage).removeCash(expected);
    }

    @Test
    @DisplayName("Снятие суммы, не кратной 100")
    void takeAmountNotMultiplyOf100() {
        AtmException exception = assertThrows(AtmException.class, () -> atmService.take(550));
        assertEquals("Введите сумму кратную 100", exception.getMessage());
    }

    @Test
    @DisplayName("Снятие при недостаточном балансе")
    void takeInsufficientFunds() {
        when(storage.getCash()).thenReturn(
                Map.of(RUB_1000, 1)
        );
        AtmException exception = assertThrows(AtmException.class, () -> atmService.take(2000));
        assertEquals("Недостаточно денег в банкомате", exception.getMessage());
    }

    @Test
    @DisplayName("Снятие невозможной суммы заданными банкнотами")
    void takeUnchangeableByBanknotes() {
        when(storage.getCash()).thenReturn(
                Map.of(RUB_1000, 2)
        );
        AtmException exception = assertThrows(AtmException.class, () -> atmService.take(1500));
        assertEquals("Невозможно выдать сумму заданными банкнотами", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка вычисления баланса")
    void getBalanceTest() {
        when(storage.getCash()).thenReturn(
                Map.of(RUB_1000, 3, RUB_500, 2)
        );
        assertEquals(4000, atmService.getBalance());
    }
}
