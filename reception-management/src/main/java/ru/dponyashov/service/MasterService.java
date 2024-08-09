package ru.dponyashov.service;

import ru.dponyashov.dto.MasterDto;

import java.util.List;
import java.util.Optional;

public interface MasterService {
    List<MasterDto> findAllWithFilter(String filterName, String filterPhone);

    MasterDto createMaster(String name, String phone);

    Optional<MasterDto> findMasterById(Long masterId);

    void updateMaster(Long masterId, String name, String phone);

    void deleteMaster(Long id);
}
