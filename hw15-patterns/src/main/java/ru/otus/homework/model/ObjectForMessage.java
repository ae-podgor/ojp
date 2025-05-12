package ru.otus.homework.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class ObjectForMessage {
    private List<String> data;
}
