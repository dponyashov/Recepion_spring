package ru.dponyashov.service;

import ru.dponyashov.dto.RecordDto;
import ru.dponyashov.enums.RecordStatus;

import java.util.List;
import java.util.Optional;

public interface OnlineRecordService {
    List<RecordDto> findNewRecordWithFilter(String filterName, String filterPhone);

    void changeStatusForRecord(Long recordId, RecordStatus Status);

    Optional<RecordDto> findRecordById(Long recordId);
}