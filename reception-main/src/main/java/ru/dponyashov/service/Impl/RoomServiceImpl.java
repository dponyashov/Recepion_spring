package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dponyashov.entity.Room;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.repository.RoomRepository;
import ru.dponyashov.service.RoomService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<Room> findAll(){
        return roomRepository.findAll();
    }

    @Override
    public Room findById(Long id){
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Room", "id", String.valueOf(id)));
    }

    @Override
    public List<Room> findByNumber(String number){
        if (number == null || number.isBlank()){
            return roomRepository.findAll();
        } else {
            return roomRepository.findByNumber("%" + number + "%");
        }
    }

    @Override
    @Transactional
    public void delete(Long id){
        roomRepository.deleteById(id);
        log.info(String.format("Удалено помещение с id: %s", id));
    }

    @Override
    @Transactional
    public Room save(Room room){
        Room savedRoom = roomRepository.save(room);
        log.info(String.format("Записаны данные помещения с id: %s", savedRoom.getId()));
        return savedRoom;
    }
}
