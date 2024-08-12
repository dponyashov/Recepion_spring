package ru.dponyashov.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ClientDto(
        Long id,
        String name,
        String phone,
        String mail,
        List<NotificationDto> notifications
) {
}
