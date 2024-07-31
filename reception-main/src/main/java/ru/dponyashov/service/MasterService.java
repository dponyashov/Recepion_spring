package ru.dponyashov.service;

import ru.dponyashov.dto.filter.FilterMaster;
import ru.dponyashov.entity.Master;

import java.util.List;

public interface MasterService {

    List<Master> findAll();

    Master findById(Long id);

    List<Master> findByName(String name);

    Master save(Master master);

    void delete(Long id);

    List<Master> findWithFilter(FilterMaster filterMaster);
}
