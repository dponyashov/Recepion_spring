package ru.dponyashov.service;

import ru.dponyashov.entity.Room;

import java.util.List;

public interface RoomService {
    List<Room> findAll();

    Room findById(Long id);

    List<Room> findByNumber(String number);

    void delete(Long id);

    Room save(Room room);
}
