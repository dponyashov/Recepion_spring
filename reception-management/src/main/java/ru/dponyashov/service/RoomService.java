package ru.dponyashov.service;

import ru.dponyashov.dto.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<RoomDto> findAllWithFilter(String filterNumber);

    RoomDto createRoom(String number, String note);

    void updateRoom(Long id, String number, String note);

    void deleteMaster(Long id);

    Optional<RoomDto> findRoomById(Long roomId);
}