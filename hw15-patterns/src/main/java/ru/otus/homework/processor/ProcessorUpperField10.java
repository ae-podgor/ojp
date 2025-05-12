package ru.otus.homework.processor;


import ru.otus.homework.model.Message;

public class ProcessorUpperField10 implements Processor {

    @Override
    public Message process(Message message) {
        message.setField4(message.getField10().toUpperCase());
        return message;
    }
}
