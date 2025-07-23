package ru.otus.homework.crm.service;


import ru.otus.homework.crm.model.Client;
import ru.otus.homework.dto.ClientCreateDto;

import java.util.List;

public interface DBServiceClient {

    List<Client> findAllClients();

    Client createClient(ClientCreateDto clientDto);
}
