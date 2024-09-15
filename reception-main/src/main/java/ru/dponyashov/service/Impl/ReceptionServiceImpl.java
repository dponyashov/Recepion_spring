package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dponyashov.dto.ReceptionDto;
import ru.dponyashov.dto.filter.FilterReception;
import ru.dponyashov.entity.Reception;
import ru.dponyashov.exception.MasterIsOccupiedException;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.exception.RoomIsOccupiedException;
import ru.dponyashov.exception.StartFinishTimeException;
import ru.dponyashov.kafka.KafkaProducer;
import ru.dponyashov.mappers.Mapper;
import ru.dponyashov.repository.ReceptionRepository;
import ru.dponyashov.safety.DataEncoder;
import ru.dponyashov.service.ReceptionService;
import ru.dponyashov.utils.NotifyCode;
import ru.dponyashov.utils.StringUtils;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReceptionServiceImpl implements ReceptionService {

    private final ReceptionRepository receptionRepository;

    private final KafkaProducer kafkaProducer;
    private final DataEncoder dataEncoder;

    private final Mapper<Reception, ReceptionDto> receptionMapper;

    @Override
    public List<ReceptionDto> findAll(){
        return receptionRepository.findAll().stream()
                .map(reception->dataEncoder.decode(receptionMapper.toDto(reception)))
                .toList();
    }

    @Override
    public ReceptionDto findById(Long id){
        Reception receptionFromDB = receptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Reception", "id", String.valueOf(id)));

        return dataEncoder.decode(receptionMapper.toDto(receptionFromDB));
    }

    @Override
    public ReceptionDto save(ReceptionDto receptionDto){
        Reception reception = dataEncoder.encode(receptionMapper.toEntity(receptionDto));

        if(reception.getFinishTime() == null){
            reception.setFinishTime(reception.getStartTime());
        }

        if(startFinishTimeIsValid(reception)){
            throw new StartFinishTimeException();
        }

        if(roomIsOccupied(reception)){
            throw new RoomIsOccupiedException(reception);
        }

        if(masterIsOccupied(reception)){
            throw new MasterIsOccupiedException(reception);
        }

        Reception savedReception = receptionRepository.save(reception);
        ReceptionDto savedReceptionDto = dataEncoder.decode(receptionMapper.toDto(savedReception));

        if(savedReception.getClient().getNotifications() != null){
            sendNotifications(savedReceptionDto);
        }
        log.info("Записаны данные записи с id: {}", savedReceptionDto.id());
        return savedReceptionDto;
    }

    @Override
    public void delete(Long id){
        receptionRepository.deleteById(id);
        log.info("Удалена запись с id: {}", id);
    }

    @Override
    public List<ReceptionDto> findWithFilter(FilterReception filterReception) {
        var encodeFilter = dataEncoder.encode(filterReception);
        return receptionRepository.findWithFilter(
                        StringUtils.stringFilterPattern(encodeFilter.getClientName()),
                        StringUtils.stringFilterPattern(encodeFilter.getMasterName()),
                        StringUtils.stringFilterPattern(encodeFilter.getRoomNumber()),
                        encodeFilter.getDateOfVisit(),
                        encodeFilter.getIdClient(),
                        encodeFilter.getIdMaster(),
                        encodeFilter.getIdRoom(),
                        pageableOrDefault(encodeFilter.getPageSize(), encodeFilter.getPageNumber())
                ).stream()
                .map(entity->dataEncoder.decode(receptionMapper.toDto(entity)))
                .toList();
    }

    @Override
    public void sendNotifications(Long id) {
        ReceptionDto reception = findById(id);
        sendNotifications(reception);
        log.info("Отправлены оповещения по записи id: {}", id);
    }

    private void sendNotifications(ReceptionDto reception) {
        reception.client().getNotifications().stream()
                .map(notification -> NotifyCode.valueOf(notification.code()))
                .forEach((code) -> sendNotify(code, reception));
    }

    private void sendNotify(NotifyCode code, ReceptionDto reception) {
        switch(code) {
            case PHONE -> kafkaProducer.sendPhone(reception);
            case MAIL -> kafkaProducer.sendMail(reception);
        }
    }

    private Pageable pageableOrDefault(int pageSize, int pageNumber) {
        if ( pageSize <= 0 ) {
            pageSize = 25;
        }
        if ( pageNumber < 0 ) {
            pageNumber = 0;
        }
        return Pageable.ofSize(pageSize)
                .withPage(pageNumber);
    }

    private Pageable pageableOrDefault() {
        return pageableOrDefault(25, 0);
    }

    private boolean roomIsOccupied(Reception reception) {
        List<Reception> roomListOnDate = receptionRepository.findWithFilter(
                null,
                null,
                null,
                reception.getDateOfVisit(),
                null,
                null,
                reception.getRoom().getId(),
                pageableOrDefault());

        return checkDates(roomListOnDate, reception);
    }

    private boolean masterIsOccupied(Reception reception) {
        List<Reception> masterListOnDate = receptionRepository.findWithFilter(
                null,
                null,
                null,
                reception.getDateOfVisit(),
                null,
                reception.getMaster().getId(),
                null,
                pageableOrDefault());

        return checkDates(masterListOnDate, reception);
    }

    private boolean startFinishTimeIsValid(Reception reception) {
        return reception.getStartTime().isAfter(reception.getFinishTime());
    }

    private boolean checkDates(List<Reception> receptionList, Reception reception) {
        long count = receptionList.stream()
                .filter(receptionFromDB -> (
                        (reception.getId() != null && !reception.getId().equals(receptionFromDB.getId())) &&
                                ( dateInInterval(reception.getStartTime(),
                                        receptionFromDB.getStartTime(),
                                        receptionFromDB.getFinishTime())
                                || dateInInterval(reception.getFinishTime(),
                                        receptionFromDB.getStartTime(),
                                        receptionFromDB.getFinishTime())
                                || dateInInterval(receptionFromDB.getStartTime(),
                                        reception.getStartTime(),
                                        reception.getFinishTime())
                                || dateInInterval(receptionFromDB.getFinishTime(),
                                        reception.getStartTime(),
                                        reception.getFinishTime())
                                )
                ))
                .count();
        return count > 0;
    }

    private boolean dateInInterval(LocalTime checkedTime, LocalTime startTime, LocalTime finishTime) {
        return checkedTime.isAfter(startTime) && checkedTime.isBefore(finishTime);
    }
}