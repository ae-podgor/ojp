package ru.otus.homework.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.model.Message;
import ru.otus.homework.provider.TimeProvider;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionProcessorTest {

    @Test
    @DisplayName("Тестируем выброс исключения при четной секунде")
    void throwsExceptionOnEvenSecond() {
        TimeProvider timeProvider = () -> LocalDateTime.of(2024, 6, 1, 12, 0, 2);
        var processor = new ExceptionProcessor(timeProvider);

        var message = Message.builder().id(1).build();

        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    @DisplayName("Тестируем НЕ выброс исключения при нечетной секунде")
    void doesNotThrowExceptionOnOddSecond() {
        TimeProvider timeProvider = () -> LocalDateTime.of(2024, 6, 1, 12, 0, 7);
        var processor = new ExceptionProcessor(timeProvider);

        var message = Message.builder().id(1).build();

        assertDoesNotThrow(() -> processor.process(message));
    }

}