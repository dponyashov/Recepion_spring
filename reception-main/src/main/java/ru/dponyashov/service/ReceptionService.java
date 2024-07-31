package ru.dponyashov.service;

import ru.dponyashov.dto.filter.FilterReception;
import ru.dponyashov.entity.Reception;

import java.util.List;

public interface ReceptionService {
    List<Reception> findAll();

    Reception findById(Long id);

    void sendNotifications(Long id);

    Reception save(Reception reception);

    void delete(Long id);

    List<Reception> findWithFilter(FilterReception filterReception);
}
