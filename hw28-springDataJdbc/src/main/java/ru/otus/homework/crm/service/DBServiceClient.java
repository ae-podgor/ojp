package ru.otus.homework.crm.service;


import ru.otus.homework.crm.model.Client;

import java.util.List;

public interface DBServiceClient {

    List<Client> findAllClients();

    Client createClient(String name, String street, String phones);
}
