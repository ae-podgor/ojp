package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.service.DBServiceClient;
import ru.otus.homework.dto.ClientCreateDto;

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
            @ModelAttribute ClientCreateDto clientDto,
            RedirectAttributes redirectAttributes
    ) {
        Client client = clientService.createClient(clientDto);
        redirectAttributes.addFlashAttribute("clientCreated",
                String.format("%s, %s, %s", client.getName(), client.getAddress().street(), client.getPhones()));
        return "redirect:/clients";
    }
}
