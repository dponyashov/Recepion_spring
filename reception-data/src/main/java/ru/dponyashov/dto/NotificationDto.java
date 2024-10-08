package ru.dponyashov.dto;

import lombok.Builder;

@Builder
public record NotificationDto(
        Long id,
        String code,
        String name
){
}
