package ru.otus.homework.services;

import ru.otus.homework.crm.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserAuthServiceImpl implements UserAuthService {

    private final Map<String, User> users;

    public UserAuthServiceImpl() {
        users = new HashMap<>();
        users.put("user1", new User(1L, "Крис Гир", "user1", "password"));
    }

    @Override
    public boolean authenticate(String login, String password) {
        User user = users.get(login);
        return user != null && user.getPassword().equals(password);
    }
}
