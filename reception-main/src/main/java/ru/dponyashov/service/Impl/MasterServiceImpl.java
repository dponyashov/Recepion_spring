package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dponyashov.dto.filter.FilterMaster;
import ru.dponyashov.entity.Master;
import ru.dponyashov.exception.NotFoundEntityException;
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

    @Override
    public List<Master> findAll(){
        return masterRepository.findAll().stream()
                .peek(dataEncoder::decode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Master> findWithFilter(FilterMaster filterMaster) {
        var encodeFilter = dataEncoder.encode(filterMaster);
        return masterRepository.findWithFilter(
                        StringUtils.stringFilterPattern(encodeFilter.getMasterName()),
                        StringUtils.stringFilterPattern(encodeFilter.getMasterPhone()),
                        Pageable.ofSize((encodeFilter.getPageSize() <= 0 ? 25 : encodeFilter.getPageSize()))
                                .withPage(encodeFilter.getPageNumber())
                ).stream()
                .peek(dataEncoder::decode)
                .toList();
    }

    @Override
    public Master findById(Long id){
        return dataEncoder.decode(masterRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Master", "id", String.valueOf(id)))
        );
    }

    @Override
    public List<Master> findByName(String name){
        return masterRepository.findByName(name).stream()
                .peek(dataEncoder::decode)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Master save(Master master){
        Master savedMaster = masterRepository.save(dataEncoder.encode(master));
        log.info(String.format("Записаны данные мастера с id: %s", savedMaster.getId()));
        return savedMaster;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        masterRepository.deleteById(id);
        log.info(String.format("Удален мастер с id: %s", id));
    }
}
