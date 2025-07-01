package ru.otus.homework.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.service.DBServiceClient;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private final transient DBServiceClient dbServiceClient;
    private final transient ObjectMapper mapper;

    public ClientsApiServlet(DBServiceClient dbServiceClient, ObjectMapper mapper) {
        this.dbServiceClient = dbServiceClient;
        this.mapper = mapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Client> all = dbServiceClient.findAll();
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try {
            out.print(mapper.writeValueAsString(all));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize client", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var client = extractClient(request);
        if (client == null || client.getName().isEmpty()) {
            throw new RuntimeException("Client or name is empty");
        }
        Client newClient = dbServiceClient.saveClient(client);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try {
            out.print(mapper.writeValueAsString(newClient));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize client", e);
        }
    }

    private Client extractClient(HttpServletRequest request) throws IOException {
        try {
            String requestBody = request.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);
            return mapper.readValue(requestBody, Client.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to deserialize client", e);
        }
    }


}
