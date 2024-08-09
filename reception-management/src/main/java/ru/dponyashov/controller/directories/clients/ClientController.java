package ru.dponyashov.controller.directories.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.ClientService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/directories/clients/{clientId:\\d+}")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @ModelAttribute("client")
    public ClientDto client(@PathVariable("clientId") Long clientId){
        return clientService.findClientById(clientId)
                .orElseThrow(() -> new NoFoundDirectoryElementException("Клиент не найден"));
    }

    @GetMapping
    public String getClientPage(){
        return "directory/clients/client";
    }

    @GetMapping("edit")
    public String getClientEditPage(){
        return "directory/clients/edit_client";
    }

    @PostMapping("edit")
    public String updateClient(@ModelAttribute(value = "client", binding = false) ClientDto client,
                               ClientDto newClient, Model model){
        try {
            clientService.updateClient(client.id(), newClient.name(), newClient.phone(), newClient.mail());
            return "redirect:/directories/clients/%d".formatted(client.id());
        } catch(BadRequestException exception){
            model.addAttribute("new_client", newClient);
            model.addAttribute("errors", exception.getErrors());
            return "/directory/clients/edit_client";
        }
    }

    @PostMapping("delete")
    public String deleteClient(@ModelAttribute("client") ClientDto client){
        clientService.deleteClient(client.id());
        return "redirect:/directories/clients";
    }
}
