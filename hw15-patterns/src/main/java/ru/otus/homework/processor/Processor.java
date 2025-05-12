package ru.otus.homework.processor;


import ru.otus.homework.model.Message;

@SuppressWarnings("java:S1135")
public interface Processor {

    Message process(Message message);

}
