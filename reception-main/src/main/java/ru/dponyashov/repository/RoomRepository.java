package ru.dponyashov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dponyashov.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(name = "Room.findByNumber", nativeQuery = true)
    List<Room> findByNumber(@Param("roomNumber") String roomNumber);
}