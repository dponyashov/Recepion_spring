package ru.dponyashov.service;

import ru.dponyashov.dto.filter.FilterClient;
import ru.dponyashov.entity.Client;
import ru.dponyashov.entity.Notification;

import java.util.List;

public interface ClientService {
    List<Client> findAll();

    Client findById(Long id);

    List<Client> findByName(String name);

    void delete(Long id);

    Client save(Client client);

    List<Client> findWithFilter(FilterClient filterClient);

    Notification findNotifyById(Long notifyId);

    List<Notification> findAllNotification();
}
