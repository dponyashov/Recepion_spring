package ru.dponyashov.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dponyashov.dto.RecordDto;
import ru.dponyashov.dto.RecordFilter;
import ru.dponyashov.entity.RecordEntity;
import ru.dponyashov.enums.RecordStatus;
import ru.dponyashov.exception.*;
import ru.dponyashov.repository.RecordRepository;
import ru.dponyashov.service.RecordService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    @Override
    public List<RecordDto> findAll() {
        return recordRepository.findAll().stream()
                .map(entity -> RecordDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .phone(entity.getPhone())
                        .note(entity.getNote())
                        .status(entity.getStatus())
                        .build()
                )
                .toList();
    }

    @Override
    public RecordDto findById(Long id) {
        return recordRepository.findById(id).stream()
                .map(entity -> RecordDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .phone(entity.getPhone())
                        .note(entity.getNote())
                        .status(entity.getStatus())
                        .build())
                .findFirst()
                .orElseThrow(NotFoundRecordException::new);
    }

    @Override
    public List<RecordDto> findWithFilter(RecordFilter filter) {

        return recordRepository.findByFilter(
                        (filter.getPhone() != null ? "%" + filter.getPhone().trim() + "%" : null),
                        (filter.getName() != null ? "%" + filter.getName().trim() + "%" : null),
                        filter.getStatus()
                ).stream()
                .map(entity ->
                        RecordDto.builder()
                                .id(entity.getId())
                                .phone(entity.getPhone())
                                .name(entity.getName())
                                .note(entity.getNote())
                                .status(entity.getStatus())
                                .build()
                )
                .toList();
    }

    @Override
    public RecordDto save(RecordDto record) {
        if(!recordIsValid(record)) {
            throw new InvalidDataException("данные записи введены не верно!");
        }

        RecordFilter filter = RecordFilter.builder()
                .phone(record.getPhone())
                .status(RecordStatus.NEW)
                .build();

        List<RecordDto> withFilter = findWithFilter(filter);
        if(!withFilter.isEmpty()){
            throw new RecordInWorkException();
        }

        return saveDto(record);
    }

    @Override
    @Transactional
    public RecordDto save(Long id, RecordDto record) {
        if(!recordIsValid(record)) {
            throw new InvalidDataException("данные записи введены не верно!");
        }
        return saveDto(id, record);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        recordRepository.deleteById(id);
        log.info("Удалены данные онлайн записи с id: {}", id);
    }

    private boolean recordIsValid(RecordDto record) {
        if(record.getPhone().isEmpty() || record.getPhone().length() < 10){
            throw new PhoneIsInvalidException();
        }
        if(record.getName().isEmpty()){
            throw new NameIsInvalidException();
        }
        return true;
    }

    private RecordDto saveDto(RecordDto record) {
        return saveDto(null, record);
    }

    private RecordDto saveDto(Long id, RecordDto record) {
        RecordEntity entity = RecordEntity.builder()
                .id(id)
                .phone(record.getPhone())
                .name(record.getName())
                .note(record.getNote())
                .status(record.getStatus())
                .build();

        RecordEntity savedEntity = recordRepository.save(entity);
        log.info("Записаны данные онлайн записи с id: {}", savedEntity.getId());
        return RecordDto.builder()
                .id(savedEntity.getId())
                .phone(savedEntity.getPhone())
                .name(savedEntity.getName())
                .note(savedEntity.getNote())
                .status(savedEntity.getStatus())
                .build();
    }
}