package ru.otus.homework.processor;


import ru.otus.homework.model.Message;

public class ProcessorConcatFields implements Processor {

    @Override
    public Message process(Message message) {
        var newFieldValue = String.join(" ", "concat:", message.getField1(), message.getField2(), message.getField3());
        message.setField4(newFieldValue);
        return message;
    }
}
