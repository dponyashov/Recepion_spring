package ru.dponyashov.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.entity.Notification;
import ru.dponyashov.mappers.Mapper;

@RequiredArgsConstructor
@Component
public class NotificationMapper implements Mapper<Notification, NotificationDto> {
    @Override
    public Notification toEntity(NotificationDto dto) {
        if(dto == null) {
            return null;
        }
        return Notification.builder()
                .id(dto.id())
                .name(dto.name())
                .code(dto.code())
                .build();
    }

    @Override
    public NotificationDto toDto(Notification entity) {
        if(entity == null) {
            return null;
        }
        return NotificationDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .build();
    }
}
