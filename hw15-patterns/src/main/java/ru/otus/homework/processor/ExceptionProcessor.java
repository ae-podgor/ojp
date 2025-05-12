package ru.otus.homework.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.model.Message;
import ru.otus.homework.provider.TimeProvider;

@RequiredArgsConstructor
public class ExceptionProcessor implements Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerProcessor.class);


    private final TimeProvider timeProvider;

    @Override
    public Message process(Message message) {
        int second = timeProvider.getDateTime().getSecond();
        if (second % 2 == 0) {
            LOGGER.debug("Second is even, throwing an exception");
            throw new RuntimeException("Oops! Second %d".formatted(second));
        }
        return message;
    }
}
