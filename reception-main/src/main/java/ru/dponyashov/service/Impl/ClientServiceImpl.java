package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dponyashov.dto.filter.FilterClient;
import ru.dponyashov.entity.Client;
import ru.dponyashov.entity.Notification;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.repository.ClientRepository;
import ru.dponyashov.repository.NotificationRepository;
import ru.dponyashov.safety.DataEncoder;
import ru.dponyashov.service.ClientService;
import ru.dponyashov.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ClientServiceImpl implements ClientService {

    private final DataEncoder dataEncoder;
    private final ClientRepository clientRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public List<Client> findAll(){
        return clientRepository.findAll().stream()
                .peek(dataEncoder::decode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Client> findWithFilter(FilterClient filterClient) {
        var encodeFilter = dataEncoder.encode(filterClient);
        return clientRepository.findWithFilter(
                        StringUtils.stringFilterPattern(encodeFilter.getClientName()),
                        StringUtils.stringFilterPattern(encodeFilter.getClientPhone()),
                        Pageable.ofSize((encodeFilter.getPageSize() <= 0 ? 25 : encodeFilter.getPageSize()))
                                .withPage(encodeFilter.getPageNumber())
                ).stream()
                .peek(dataEncoder::decode)
                .toList();
    }

    @Override
    public Notification findNotifyById(Long notifyId) {
        return notificationRepository.findById(notifyId)
                .orElseThrow(()-> new NotFoundEntityException("Notification", "id", String.valueOf(notifyId)));
    }

    @Override
    public List<Notification> findAllNotification() {
        return notificationRepository.findAllNotification();
    }

    @Override
    public Client findById(Long id){
        return dataEncoder.decode(clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Client", "id", String.valueOf(id)))
        );
    }

    @Override
    public List<Client> findByName(String name){
        return clientRepository.findByName(name).stream()
                .peek(dataEncoder::decode)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clientRepository.deleteById(id);
        log.info("Удален клиент с id: {}", id);
    }

    @Override
    @Transactional
    public Client save(Client client){
        Client clientForSave = Client.builder().
                id(client.getId()).
                name(client.getName()).
                phone(client.getPhone()).
                mail(client.getMail()).
                notifications(client.getNotifications()).
                build();
        Client savedClient = clientRepository.save(dataEncoder.encode(clientForSave));
        log.info("Записаны данные клиента с id: {}", savedClient.getId());
        client.setId(savedClient.getId());
        return dataEncoder.decode(client);
    }
}
