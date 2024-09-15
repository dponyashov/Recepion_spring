package ru.dponyashov.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.entity.Master;
import ru.dponyashov.mappers.Mapper;

@RequiredArgsConstructor
@Component
public class MasterMapper implements Mapper<Master, MasterDto> {
    @Override
    public Master toEntity(MasterDto dto) {
        if(dto == null) {
            return null;
        }

        return Master.builder()
                .id(dto.getId())
                .name(dto.getName())
                .phone(dto.getPhone())
                .build();
    }

    @Override
    public MasterDto toDto(Master entity) {
        if(entity == null) {
            return null;
        }

        return MasterDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .build();
    }
}
