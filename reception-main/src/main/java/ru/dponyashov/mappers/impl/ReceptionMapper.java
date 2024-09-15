package ru.dponyashov.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.dto.ReceptionDto;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.entity.Client;
import ru.dponyashov.entity.Master;
import ru.dponyashov.entity.Reception;
import ru.dponyashov.entity.Room;
import ru.dponyashov.mappers.Mapper;


@RequiredArgsConstructor
@Component
public class ReceptionMapper implements Mapper<Reception, ReceptionDto> {

    final Mapper<Master, MasterDto> masterMapper;
    final Mapper<Client, ClientDto> clientMapper;
    final Mapper<Room, RoomDto> roomMapper;

    @Override
    public Reception toEntity(ReceptionDto dto) {
        if(dto == null) {
            return null;
        }
        return Reception.builder()
                .id(dto.id())
                .client(clientMapper.toEntity(dto.client()))
                .master(masterMapper.toEntity(dto.master()))
                .room(roomMapper.toEntity(dto.room()))
                .dateOfVisit(dto.dateOfVisit())
                .startTime(dto.startTime())
                .finishTime(dto.finishTime())
                .details(dto.details())
                .build();
    }

    @Override
    public ReceptionDto toDto(Reception entity) {
        if(entity == null) {
            return null;
        }
        return ReceptionDto.builder()
                .id(entity.getId())
                .client(clientMapper.toDto(entity.getClient()))
                .master(masterMapper.toDto(entity.getMaster()))
                .room(roomMapper.toDto(entity.getRoom()))
                .dateOfVisit(entity.getDateOfVisit())
                .startTime(entity.getStartTime())
                .finishTime(entity.getFinishTime())
                .details(entity.getDetails())
                .build();
    }
}
