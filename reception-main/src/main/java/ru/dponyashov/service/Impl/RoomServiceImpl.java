package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.entity.Room;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.mappers.Mapper;
import ru.dponyashov.repository.RoomRepository;
import ru.dponyashov.service.RoomService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final Mapper<Room, RoomDto> roomMapper;

    @Override
    public List<RoomDto> findAll(){
        return roomRepository.findAll().stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Override
    public RoomDto findById(Long id){
        return roomMapper.toDto(roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Room", "id", String.valueOf(id)))
        );
    }

    @Override
    public List<RoomDto> findByNumber(String number){
        if (number == null || number.isBlank()){
            return findAll();
        } else {
            return roomRepository.findByNumber("%" + number + "%").stream()
                    .map(roomMapper::toDto)
                    .toList();
        }
    }

    @Override
    public void delete(Long id){
        roomRepository.deleteById(id);
        log.info("Удалено помещение с id: {}", id);
    }

    @Override
    public RoomDto save(RoomDto room){
        Room roomForSave = roomMapper.toEntity(room);
        Room savedRoom = roomRepository.save(roomForSave);
        log.info("Записаны данные помещения с id: {}", savedRoom.getId());
        return roomMapper.toDto(savedRoom);
    }
}
