package ru.dponyashov.dto;

import lombok.Builder;

@Builder
public record Response(String message) {
}
