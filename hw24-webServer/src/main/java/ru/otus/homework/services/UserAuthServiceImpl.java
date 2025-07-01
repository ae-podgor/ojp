package ru.otus.homework.services;

import ru.otus.homework.crm.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserAuthServiceImpl implements UserAuthService {

    private final Map<Long, User> users;

    public UserAuthServiceImpl() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "Крис Гир", "user1", "password"));
    }

    @Override
    public boolean authenticate(String login, String password) {
        return users.values().stream().filter(v -> v.getLogin().equals(login)).findFirst()
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
