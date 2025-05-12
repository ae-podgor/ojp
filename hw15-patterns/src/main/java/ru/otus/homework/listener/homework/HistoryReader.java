package ru.otus.homework.listener.homework;

import ru.otus.homework.model.Message;

import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
