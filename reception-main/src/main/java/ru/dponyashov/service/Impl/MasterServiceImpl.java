package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.dto.filter.FilterMaster;
import ru.dponyashov.entity.Master;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.mappers.Mapper;
import ru.dponyashov.repository.MasterRepository;
import ru.dponyashov.safety.DataEncoder;
import ru.dponyashov.service.MasterService;
import ru.dponyashov.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MasterServiceImpl implements MasterService {

    private final DataEncoder dataEncoder;
    private final MasterRepository masterRepository;
    private final Mapper<Master, MasterDto> masterMapper;

    @Override
    public List<MasterDto> findAll(){
        return masterRepository.findAll().stream()
                .map(master -> dataEncoder.decode(masterMapper.toDto(master)))
                .toList();
    }

    @Override
    public List<MasterDto> findWithFilter(FilterMaster filterMaster) {
        var encodeFilter = dataEncoder.encode(filterMaster);
        return masterRepository.findWithFilter(
                        StringUtils.stringFilterPattern(encodeFilter.getMasterName()),
                        StringUtils.stringFilterPattern(encodeFilter.getMasterPhone()),
                        Pageable.ofSize((encodeFilter.getPageSize() <= 0 ? 25 : encodeFilter.getPageSize()))
                                .withPage(encodeFilter.getPageNumber())
                ).stream()
                .map(master -> dataEncoder.decode(masterMapper.toDto(master)))
                .toList();
    }

    @Override
    public MasterDto findById(Long id){
        return dataEncoder.decode(masterMapper.toDto(
                masterRepository.findById(id)
                        .orElseThrow(() -> new NotFoundEntityException("Master", "id", String.valueOf(id)))
                )
        );
    }

    @Override
    public List<MasterDto> findByName(String name){
        return masterRepository.findByName(name).stream()
                .map(master -> dataEncoder.decode(masterMapper.toDto(master)))
                .collect(Collectors.toList());
    }

    @Override
    public MasterDto save(MasterDto masterDto){
        Master masterForSave = masterMapper.toEntity(dataEncoder.encode(masterDto));
        Master savedMaster = masterRepository.save(masterForSave);
        MasterDto savedMasterDto = masterMapper.toDto(dataEncoder.decode(savedMaster));

        log.info("Записаны данные мастера с id: {}", savedMasterDto.getId());
        return savedMasterDto;
    }

    @Override
    public void delete(Long id) {
        masterRepository.deleteById(id);
        log.info("Удален мастер с id: {}", id);
    }
}
