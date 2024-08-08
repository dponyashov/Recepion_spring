package ru.dponyashov.service;

import ru.dponyashov.dto.RecordDto;
import ru.dponyashov.dto.RecordFilter;

import java.util.List;

public interface RecordService {
    List<RecordDto> findAll();

    RecordDto findById(Long id);

    List<RecordDto> findWithFilter(RecordFilter filter);

    RecordDto save(RecordDto record);

    RecordDto save(Long id, RecordDto record);

    void delete(Long id);
}