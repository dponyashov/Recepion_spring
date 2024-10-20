package ru.dponyashov.controller.directories.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.exception.NotFoundDirectoryElementException;
import ru.dponyashov.service.ClientService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/directories/clients/{clientId:\\d+}")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @ModelAttribute("client")
    public ClientDto client(@PathVariable("clientId") Long clientId){
        return clientService.findClientById(clientId)
                .orElseThrow(() -> new NotFoundDirectoryElementException("Клиент не найден"));
    }

    @ModelAttribute("notificationList")
    public List<NotificationDto> notifications(){
        return clientService.notificationList();
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
                               ClientDto newClient,
                               @RequestParam(value = "notificationList", required = false) Long[] selectedNotifications,
                               Model model){
        List<NotificationDto> selectedNotify = new ArrayList<>();
        if(selectedNotifications != null) {
            selectedNotify = Arrays.stream(selectedNotifications)
                    .map(id -> clientService.findNotifyById(id).orElse(null))
                    .toList();
        }
        try {
            clientService.updateClient(client.getId(), newClient.getName(), newClient.getPhone(), newClient.getMail(),
                    selectedNotify);
            return "redirect:/directories/clients/%d".formatted(client.getId());
        } catch(BadRequestException exception){
            model.addAttribute("new_client", newClient);
            model.addAttribute("errors", exception.getErrors());
            return "/directory/clients/edit_client";
        }
    }

    @GetMapping("delete")
    public String deleteClientPage(@ModelAttribute("client") ClientDto client){
        return "directory/clients/delete_client";
    }

    @PostMapping("delete")
    public String deleteClient(@ModelAttribute("client") ClientDto client){
        clientService.deleteClient(client.getId());
        return "redirect:/directories/clients";
    }
}
