package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.handler.ComplexProcessor;
import ru.otus.homework.listener.ListenerPrinterConsole;
import ru.otus.homework.model.Message;
import ru.otus.homework.processor.LoggerProcessor;
import ru.otus.homework.processor.ProcessorConcatFields;
import ru.otus.homework.processor.ProcessorUpperField10;

import java.util.List;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(), new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = Message.builder()
                .id(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
