package ru.dponyashov.dto;

import lombok.Builder;

@Builder
public record ClientDto(
        Long id,
        String name,
        String phone,
        String mail
) {
}
