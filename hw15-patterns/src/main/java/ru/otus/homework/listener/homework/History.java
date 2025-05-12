package ru.otus.homework.listener.homework;

import lombok.Getter;
import lombok.ToString;
import ru.otus.homework.model.Message;
import ru.otus.homework.model.ObjectForMessage;

import java.util.ArrayList;

@ToString
@Getter
public class History {
    private final Message message;

    History(Message message) {
        this.message = deepCopy(message);
    }

    private Message deepCopy(Message message) {
        if (message == null) return null;

        ObjectForMessage field13 = deepCopyObjectForMessage(message.getField13());

        return Message.builder()
                .id(message.getId())
                .field1(message.getField1())
                .field2(message.getField2())
                .field3(message.getField3())
                .field4(message.getField4())
                .field5(message.getField5())
                .field6(message.getField6())
                .field7(message.getField7())
                .field8(message.getField8())
                .field9(message.getField9())
                .field10(message.getField10())
                .field11(message.getField11())
                .field12(message.getField12())
                .field13(field13)
                .build();
    }

    private ObjectForMessage deepCopyObjectForMessage(ObjectForMessage original) {
        if (original == null) return null;
        ObjectForMessage copy = new ObjectForMessage();
        if (original.getData() != null) {
            copy.setData(new ArrayList<>(original.getData()));
        }
        return copy;
    }

}
