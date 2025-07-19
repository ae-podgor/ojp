package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.service.DBServiceClient;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final DBServiceClient clientService;

    @GetMapping({"/", "/clients"})
    public String listClients(Model model) {
        List<Client> clients = clientService.findAllClients();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @PostMapping("/clients/create")
    public String createClient(
            @RequestParam String name,
            @RequestParam String street,
            @RequestParam String phones, // строки через запятую
            Model model
    ) {
        Client client = clientService.createClient(name, street, phones);
        model.addAttribute("clientCreated",
                String.format("%s, %s, %s", client.getName(), client.getAddress().street(), client.getPhones()));
        model.addAttribute("clients", clientService.findAllClients());
        return "clients";
    }
}
