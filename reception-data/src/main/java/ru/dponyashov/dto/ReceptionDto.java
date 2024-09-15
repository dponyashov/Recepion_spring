package ru.dponyashov.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ReceptionDto(
        Long id,
        MasterDto master,
        RoomDto room,
        ClientDto client,
        LocalDate dateOfVisit,
        LocalTime startTime,
        LocalTime finishTime,
        String details
) {
}