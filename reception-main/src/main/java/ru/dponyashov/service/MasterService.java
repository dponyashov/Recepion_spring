package ru.dponyashov.service;

import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.dto.filter.FilterMaster;

import java.util.List;

public interface MasterService {

    List<MasterDto> findAll();

    MasterDto findById(Long id);

    List<MasterDto> findByName(String name);

    MasterDto save(MasterDto master);

    void delete(Long id);

    List<MasterDto> findWithFilter(FilterMaster filterMaster);
}
