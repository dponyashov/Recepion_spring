package ru.dponyashov.controller.directories.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.ClientService;

@Controller
@RequestMapping("/directories/clients")
@RequiredArgsConstructor
public class ClientListController {

    private final ClientService clientService;

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
    public String getNewClientPage(){
        return "directory/clients/new_client";
    }

    @PostMapping("create")
    public String createClient(ClientDto newClient, Model model){
        try {
            ClientDto client = clientService.createClient(newClient.name(), newClient.phone(),
                    newClient.mail());
            return "redirect:/directories/clients/%d".formatted(client.id());
        }  catch(BadRequestException exception){
            model.addAttribute("client", newClient);
            model.addAttribute("errors", exception.getErrors());
            return "directory/clients/new_client";
        }
    }
}
