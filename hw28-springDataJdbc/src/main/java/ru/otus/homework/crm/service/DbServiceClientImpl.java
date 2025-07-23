package ru.otus.homework.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.crm.model.Address;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.Phone;
import ru.otus.homework.crm.repository.AddressRepository;
import ru.otus.homework.crm.repository.ClientRepository;
import ru.otus.homework.crm.repository.PhoneRepository;
import ru.otus.homework.dto.ClientCreateDto;
import ru.otus.homework.sessionmanager.TransactionManager;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final TransactionManager transactionManager;

    @Override
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client createClient(ClientCreateDto clientDto) {
        return transactionManager.doInTransaction(() -> {
            Client client = new Client(null, clientDto.name(), null, List.of(), true);
            Client savedClient = clientRepository.save(client);

            Address address = addressRepository.save(new Address(null, clientDto.street(), savedClient.getId()));

            List<Phone> phoneNumbers = Arrays.stream(clientDto.phones().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(number -> new Phone(null, number, savedClient.getId()))
                    .map(phoneRepository::save)
                    .toList();

            return new Client(
                    savedClient.getId(),
                    savedClient.getName(),
                    address,
                    phoneNumbers,
                    false
            );
        });
    }
}
