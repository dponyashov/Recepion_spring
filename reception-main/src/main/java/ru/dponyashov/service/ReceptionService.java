package ru.dponyashov.service;

import ru.dponyashov.dto.ReceptionDto;
import ru.dponyashov.dto.filter.FilterReception;

import java.util.List;

public interface ReceptionService {
    List<ReceptionDto> findAll();

    ReceptionDto findById(Long id);

    void sendNotifications(Long id);

    ReceptionDto save(ReceptionDto reception);

    void delete(Long id);

    List<ReceptionDto> findWithFilter(FilterReception filterReception);
}
