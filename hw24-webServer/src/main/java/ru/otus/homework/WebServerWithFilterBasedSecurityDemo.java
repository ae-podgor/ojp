package ru.otus.homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.cfg.Configuration;
import ru.otus.homework.core.repository.DataTemplateHibernate;
import ru.otus.homework.core.repository.HibernateConfigManager;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.service.DbServiceClientImpl;
import ru.otus.homework.server.ClientWebServer;
import ru.otus.homework.server.ClientWebServerWithFilterBasedSecurity;
import ru.otus.homework.services.TemplateProcessor;
import ru.otus.homework.services.TemplateProcessorImpl;
import ru.otus.homework.services.UserAuthService;
import ru.otus.homework.services.UserAuthServiceImpl;

/*
    // Стартовая страница
    http://localhost:8080

    // Страница клиентов
    http://localhost:8080/clients

    // REST сервис
    http://localhost:8080/api/clients
*/
public class WebServerWithFilterBasedSecurityDemo {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var transactionManager = new HibernateConfigManager(
                new Configuration()).getTransactionManager();
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
//        var phone = new Phone("+7943430430403");
//        var street = new Address("Street");
//        dbServiceClient.saveClient(new Client("dbServiceFirst", street, List.of(phone)));
//        dbServiceClient.saveClient(new Client("dbServiceSecond"));

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl();

        var objectMapper = new ObjectMapper();

        ClientWebServer clientWebServer = new ClientWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, authService,
                objectMapper, templateProcessor, dbServiceClient);

        clientWebServer.start();
        clientWebServer.join();
    }
}
