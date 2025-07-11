package ru.otus.homework.homework;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import ru.otus.homework.listener.homework.HistoryListener;
import ru.otus.homework.model.Message;
import ru.otus.homework.model.ObjectForMessage;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class HistoryListenerTest {

    @Test
    void listenerTest() {
        // given
        var historyListener = new HistoryListener();

        var id = 1L;
        var data = "33";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);

        var message = Message.builder().id(id)
                .field10("field10")
                .field13(field13)
                .build();

        // when
        historyListener.onUpdated(message);
        message.getField13().setData(new ArrayList<>()); //меняем исходное сообщение
        field13Data.clear(); //меняем исходный список

        // then
        var messageFromHistory = historyListener.findMessageById(id);
        AssertionsForClassTypes.assertThat(messageFromHistory).isPresent();
        assertThat(messageFromHistory.get().getField13().getData()).containsExactly(data);
    }
}
