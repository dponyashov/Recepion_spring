package ru.dponyashov.service;

import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.dto.ReceptionDto;
import ru.dponyashov.dto.RoomDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReceptionService {

    List<ReceptionDto> findAllWithFilter(String masterName, String clientName, String roomNumber, LocalDate dateOfVisit);

    ReceptionDto createReception(MasterDto master, RoomDto room, ClientDto client, LocalDate localDate,
                                 LocalTime localTime, LocalTime localTime1, String details);

    List<ReceptionDto> findAll();

    Optional<ReceptionDto> findReceptionById(Long receptionId);

    void deleteReception(Long receptionId);

    void updateReception(Long receptionId, MasterDto master, RoomDto room, ClientDto client,
                         LocalDate dateOfVisit, LocalTime startTime, LocalTime finishTime, String details);
}
