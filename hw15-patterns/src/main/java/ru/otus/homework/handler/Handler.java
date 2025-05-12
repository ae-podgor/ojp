package ru.otus.homework.handler;


import ru.otus.homework.listener.Listener;
import ru.otus.homework.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
