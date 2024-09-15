package ru.dponyashov.dto;

import lombok.Builder;

@Builder
public record RecordDto(
        Long id,
        String phone,
        String name,
        String note,
        String status
) {
}