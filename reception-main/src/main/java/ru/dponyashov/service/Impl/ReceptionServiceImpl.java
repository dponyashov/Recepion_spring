package ru.dponyashov.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dponyashov.dto.filter.FilterReception;
import ru.dponyashov.exception.MasterIsOccupiedException;
import ru.dponyashov.exception.RoomIsOccupiedException;
import ru.dponyashov.exception.StartFinishTimeException;
import ru.dponyashov.safety.DataEncoder;
import ru.dponyashov.utils.NotifyCode;
import ru.dponyashov.entity.*;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.kafka.KafkaProducer;
import ru.dponyashov.repository.ReceptionRepository;
import ru.dponyashov.service.ReceptionService;
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

    @Override
    public List<Reception> findAll(){
        return receptionRepository.findAll().stream()
                .peek(dataEncoder::decode)
                .toList();
    }

    @Override
    public Reception findById(Long id){
        Reception receptionFromDB = receptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Reception", "id", String.valueOf(id)));

        dataEncoder.decode(receptionFromDB);

        return receptionFromDB;
    }

    @Override
    @Transactional
    public Reception save(Reception reception){
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

        Reception savedReception = receptionRepository.save(dataEncoder.encode(reception));
        if(savedReception.getClient().getNotifications() != null){
            sendNotifications(savedReception);
        }
        log.info(String.format("Записаны данные записи с id: %s", savedReception.getId()));
        return dataEncoder.decode(reception);
    }

    @Override
    @Transactional
    public void delete(Long id){
        receptionRepository.deleteById(id);
        log.info(String.format("Удалена запись с id: %s", id));
    }

    @Override
    public List<Reception> findWithFilter(FilterReception filterReception) {
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
                .peek(dataEncoder::decode)
                .toList();
    }

    @Override
    public void sendNotifications(Long id) {
        Reception reception = findById(id);
        sendNotifications(dataEncoder.decode(reception));
        log.info(String.format("Отправлены оповещения по записи id: %s", id));
    }

    private void sendNotifications(Reception reception) {
        reception.getClient().getNotifications().stream()
                .map(notification -> NotifyCode.valueOf(notification.getCode()))
                .forEach((code) -> sendNotify(code, reception));
    }

    private void sendNotify(NotifyCode code, Reception reception) {
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