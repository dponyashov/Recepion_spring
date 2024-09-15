package ru.dponyashov.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.entity.Client;
import ru.dponyashov.entity.Notification;
import ru.dponyashov.mappers.Mapper;

@RequiredArgsConstructor
@Component
public class ClientMapper implements Mapper<Client, ClientDto> {

    private final Mapper<Notification, NotificationDto> notificationMapper;

    @Override
    public Client toEntity(ClientDto dto) {
        if(dto == null) {
            return null;
        }
        return Client.builder()
                .id(dto.getId())
                .mail(dto.getMail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .notifications(dto.getNotifications().stream()
                        .map(notificationMapper::toEntity)
                        .toList()
                )
                .build();
    }

    @Override
    public ClientDto toDto(Client entity) {
        if(entity == null) {
            return null;
        }
        return ClientDto.builder()
                .id(entity.getId())
                .mail(entity.getMail())
                .name(entity.getName())
                .phone(entity.getPhone())
                .notifications(entity.getNotifications().stream()
                        .map(notificationMapper::toDto)
                        .toList()
                )
                .build();
    }
}
