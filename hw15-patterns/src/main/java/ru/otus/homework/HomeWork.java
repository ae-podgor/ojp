package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.handler.ComplexProcessor;
import ru.otus.homework.listener.homework.HistoryListener;
import ru.otus.homework.model.Message;
import ru.otus.homework.model.ObjectForMessage;
import ru.otus.homework.processor.ExceptionProcessor;
import ru.otus.homework.processor.SwitchingProcessor;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {
    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение
        */
        var processors = List.of(new SwitchingProcessor(), new ExceptionProcessor(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });
        var listener = new HistoryListener();
        complexProcessor.addListener(listener);

        ObjectForMessage objectForMessage = new ObjectForMessage();
        objectForMessage.setData(List.of("data1", "data2", "data3"));

        var message = Message.builder()
                .id(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field4("field4")
                .field5("field5")
                .field6("field6")
                .field7("field6")
                .field8("field6")
                .field9("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listener);
    }
}
