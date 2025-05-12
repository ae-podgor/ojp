package ru.otus.homework.processor;

import ru.otus.homework.model.Message;

public class SwitchingProcessor implements Processor {

    @Override
    public Message process(Message message) {
        String prevField11 = message.getField11();
        message.setField11(message.getField12());
        message.setField12(prevField11);
        return message;
    }
}
