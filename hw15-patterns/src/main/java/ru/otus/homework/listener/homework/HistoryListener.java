package ru.otus.homework.listener.homework;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.listener.Listener;
import ru.otus.homework.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HistoryListener implements Listener, HistoryReader {

    private final List<History> history = new ArrayList<>();

    @Override
    public void onUpdated(Message msg) {
        history.add(new History(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return history.stream()
                .map(History::getMessage)
                .filter(m -> m.getId() == id)
                .findFirst();
    }
}
