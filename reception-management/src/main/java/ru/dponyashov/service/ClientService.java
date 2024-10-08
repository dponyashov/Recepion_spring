package ru.dponyashov.service;

import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.NotificationDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<ClientDto> findAllWithFilter(String filterName, String filterPhone);

    ClientDto createClient(String name, String phone, String mail, List<NotificationDto> notifications);

    Optional<ClientDto> findClientById(Long clientId);

    void updateClient(Long id, String name, String phone, String mail, List<NotificationDto> notifications);

    void deleteClient(Long id);

    List<NotificationDto> notificationList();

    Optional<NotificationDto> findNotifyById(Long ig);

    List<ClientDto> findAll();
}