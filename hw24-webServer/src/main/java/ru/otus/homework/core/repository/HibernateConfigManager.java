package ru.otus.homework.core.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.homework.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.homework.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.homework.crm.model.Address;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.Phone;

public class HibernateConfigManager {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private final Configuration configuration;

    public HibernateConfigManager(Configuration configuration) {
        this.configuration = configuration;
    }

    public TransactionManagerHibernate getTransactionManager() {
        configuration.configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        return new TransactionManagerHibernate(sessionFactory);
    }

}
