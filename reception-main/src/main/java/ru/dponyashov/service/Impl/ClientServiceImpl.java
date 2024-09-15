package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.dto.filter.FilterClient;
import ru.dponyashov.entity.Client;
import ru.dponyashov.entity.Notification;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.mappers.Mapper;
import ru.dponyashov.repository.ClientRepository;
import ru.dponyashov.repository.NotificationRepository;
import ru.dponyashov.safety.DataEncoder;
import ru.dponyashov.service.ClientService;
import ru.dponyashov.utils.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ClientServiceImpl implements ClientService {

    private final DataEncoder dataEncoder;
    private final ClientRepository clientRepository;
    private final NotificationRepository notificationRepository;

    private final Mapper<Client, ClientDto> clientMapper;
    private final Mapper<Notification, NotificationDto> notificationMapper;

    @Override
    public List<ClientDto> findAll(){
        return clientRepository.findAll().stream()
                .map(client -> dataEncoder.decode(clientMapper.toDto(client)))
                .toList();
    }

    @Override
    public List<ClientDto> findWithFilter(FilterClient filterClient) {
        var encodeFilter = dataEncoder.encode(filterClient);
        return clientRepository.findWithFilter(
                        StringUtils.stringFilterPattern(encodeFilter.getClientName()),
                        StringUtils.stringFilterPattern(encodeFilter.getClientPhone()),
                        Pageable.ofSize((encodeFilter.getPageSize() <= 0 ? 25 : encodeFilter.getPageSize()))
                                .withPage(encodeFilter.getPageNumber())
                ).stream()
                .map(client -> dataEncoder.decode(clientMapper.toDto(client)))
                .toList();
    }

    @Override
    public NotificationDto findNotifyById(Long notifyId) {
        return notificationMapper.toDto(
                notificationRepository.findById(notifyId)
                .orElseThrow(()-> new NotFoundEntityException("Notification", "id", String.valueOf(notifyId)))
        );
    }

    @Override
    public List<NotificationDto> findAllNotification() {
        return notificationRepository.findAllNotification().stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto findById(Long id){
        return dataEncoder.decode(clientMapper.toDto(
                clientRepository.findById(id)
                        .orElseThrow(() -> new NotFoundEntityException("Client", "id", String.valueOf(id)))
                )
        );
    }

    @Override
    public List<ClientDto> findByName(String name){
        return clientRepository.findByName(name).stream()
                .map(client -> dataEncoder.decode(clientMapper.toDto(client)))
                .toList();
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
        log.info("Удален клиент с id: {}", id);
    }

    @Override
    public ClientDto save(ClientDto clientDto){
        Client clientForSave = clientMapper.toEntity(dataEncoder.encode(clientDto));

        Client savedClient = clientRepository.save(clientForSave);

        ClientDto savedClientDto = dataEncoder.decode(clientMapper.toDto(savedClient));

        log.info("Записаны данные клиента с id: {}", savedClientDto.getId());
        return savedClientDto;
    }
}
