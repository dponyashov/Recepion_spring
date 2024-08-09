package ru.dponyashov.dto;

import lombok.Builder;

@Builder
public record RoomDto(
        Long id,
        String number,
        String note
)
{
}
