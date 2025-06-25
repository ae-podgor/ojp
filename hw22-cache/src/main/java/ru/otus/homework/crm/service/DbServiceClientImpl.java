package ru.otus.homework.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.HwCache;
import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.sessionmanager.TransactionManager;
import ru.otus.homework.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionManager transactionManager,
                               DataTemplate<Client> clientDataTemplate,
                               HwCache<String, Client> cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                cache.put(String.valueOf(savedClient.getId()), savedClient);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", savedClient);
            cache.put(String.valueOf(savedClient.getId()), savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = cache.get(String.valueOf(id));
        if (client != null) {
            log.info("found client in cache: {}", client);
            return Optional.of(client);
        }
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(value -> cache.put(String.valueOf(id), value));
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
