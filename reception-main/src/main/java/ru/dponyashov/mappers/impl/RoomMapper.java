package ru.dponyashov.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.entity.Room;
import ru.dponyashov.mappers.Mapper;

@RequiredArgsConstructor
@Component
public class RoomMapper implements Mapper<Room, RoomDto> {

    @Override
    public Room toEntity(RoomDto dto) {
        if(dto == null) {
            return null;
        }
        return Room.builder()
                .id(dto.id())
                .number(dto.number())
                .note(dto.note())
                .build();
    }

    @Override
    public RoomDto toDto(Room entity) {
        if(entity==null) {
            return null;
        }
        return RoomDto.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .note(entity.getNote())
                .build();
    }
}
