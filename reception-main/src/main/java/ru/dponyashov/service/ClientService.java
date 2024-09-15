package ru.dponyashov.service;

import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.dto.filter.FilterClient;

import java.util.List;

public interface ClientService {
    List<ClientDto> findAll();

    ClientDto findById(Long id);

    List<ClientDto> findByName(String name);

    void delete(Long id);

    ClientDto save(ClientDto clientDto);

    List<ClientDto> findWithFilter(FilterClient filterClient);

    NotificationDto findNotifyById(Long notifyId);

    List<NotificationDto> findAllNotification();
}
