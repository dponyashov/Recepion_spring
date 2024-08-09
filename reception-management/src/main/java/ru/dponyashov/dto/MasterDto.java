package ru.dponyashov.dto;

import lombok.Builder;

@Builder
public record MasterDto (
        Long id,
        String name,
        String phone
){
}
