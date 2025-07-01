package ru.otus.homework.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
