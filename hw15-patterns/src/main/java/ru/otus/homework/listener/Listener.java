package ru.otus.homework.listener;


import ru.otus.homework.model.Message;

@SuppressWarnings("java:S1135")
public interface Listener {

    void onUpdated(Message msg);
}
