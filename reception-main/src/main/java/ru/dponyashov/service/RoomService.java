package ru.dponyashov.service;

import ru.dponyashov.dto.RoomDto;

import java.util.List;

public interface RoomService {
    List<RoomDto> findAll();

    RoomDto findById(Long id);

    List<RoomDto> findByNumber(String number);

    void delete(Long id);

    RoomDto save(RoomDto room);
}
