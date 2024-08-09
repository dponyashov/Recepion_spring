package ru.dponyashov.service;

import ru.dponyashov.dto.ClientDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<ClientDto> findAllWithFilter(String filterName, String filterPhone);

    ClientDto createClient(String name, String phone, String mail);

    Optional<ClientDto> findClientById(Long clientId);

    void updateClient(Long id, String name, String phone, String mail);

    void deleteClient(Long id);
}