package ru.dponyashov.controller.directories.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.ClientService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/directories/clients")
@RequiredArgsConstructor
public class ClientListController {

    private final ClientService clientService;

    @ModelAttribute("notificationList")
    public List<NotificationDto> notificationList(){
        return clientService.notificationList();
    }

    @ModelAttribute("notifySelected")
    public List<NotificationDto> notifySelectedList(){
        return clientService.notificationList();
    }

    @GetMapping
    public String showClientList(Model model,
                                 @RequestParam(name = "clientName", required = false)String filterName,
                                 @RequestParam(name = "clientPhone", required = false)String filterPhone){
        model.addAttribute("clients", clientService.findAllWithFilter(filterName, filterPhone));
        model.addAttribute("filterName", filterName);
        model.addAttribute("filterPhone", filterPhone);

        return "directory/clients/client-list";
    }

    @GetMapping("create")
    public String getNewClientPage(Model model){
        return "directory/clients/new_client";
    }

    @PostMapping("create")
    public String createClient(ClientDto newClient,
                               @RequestParam(value = "notificationList", required = false)
                               Long[] selectedNotifications,
                               Model model){

        List<NotificationDto> selectedNotify = new ArrayList<>();
        if(selectedNotifications != null) {
            selectedNotify = Arrays.stream(selectedNotifications)
                    .map(id -> clientService.findNotifyById(id).orElse(null))
                    .toList();
        }
        try {
            ClientDto client = clientService.createClient(newClient.getName(), newClient.getPhone(),
                    newClient.getMail(), selectedNotify);
            return "redirect:/directories/clients/%d".formatted(client.getId());
        }  catch(BadRequestException exception){
            model.addAttribute("client", newClient);
            model.addAttribute("notifySelected", selectedNotify);
            model.addAttribute("errors", exception.getErrors());
            return "directory/clients/new_client";
        }
    }
}
