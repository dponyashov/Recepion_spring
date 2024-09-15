package ru.dponyashov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.filter.FilterClient;
import ru.dponyashov.service.ClientService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDto>> findClient(FilterClient filterClient){
        if(filterClient != null){
            return new ResponseEntity<>(clientService.findWithFilter(filterClient), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(clientService.findAll(), HttpStatus.OK);
        }
    }

    @GetMapping("/{idClient:\\d+}")
    public ResponseEntity<ClientDto> findById(@PathVariable("idClient") Long id){
        ClientDto clientFromDB =  clientService.findById(id);
        return new ResponseEntity<>(clientFromDB, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClientDto> saveClient(@Valid @RequestBody ClientDto newClient,
                                             BindingResult bindingResult,
                                             UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            ClientDto clientFromDB = clientService.save(newClient);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .path("/api/client/{clientId}")
                            .build(Map.of("clientId", clientFromDB.getId())))
                    .body(clientFromDB);
        }
    }

    @PutMapping("/{idClient:\\d+}")
    public ResponseEntity<ClientDto> saveClient(@PathVariable("idClient") Long id,
                                             @Valid @RequestBody ClientDto client,
                                             BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            ClientDto clientForSave = ClientDto.builder()
                    .id(id)
                    .name(client.getName())
                    .phone(client.getPhone())
                    .mail(client.getMail())
                    .notifications(client.getNotifications())
                    .build();
            ClientDto clientFromDB = clientService.save(clientForSave);
            return new ResponseEntity<>(clientFromDB, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idClient:\\d+}")
    public ResponseEntity<Void> deleteClient(@PathVariable("idClient") Long id){
        clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
