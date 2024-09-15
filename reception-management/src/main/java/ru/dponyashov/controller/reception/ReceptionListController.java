package ru.dponyashov.controller.reception;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.*;
import ru.dponyashov.enums.RecordStatus;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.exception.NotFoundDirectoryElementException;
import ru.dponyashov.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/receptions")
@RequiredArgsConstructor
public class ReceptionListController {

    private final ClientService clientService;
    private final MasterService masterService;
    private final RoomService roomService;
    private final ReceptionService receptionService;
    private final OnlineRecordService onlineRecordService;

    @ModelAttribute("clientList")
    public List<ClientDto> clientList(){
        return clientService.findAll();
    }

    @ModelAttribute("roomList")
    public List<RoomDto> roomList(){
        return roomService.findAll();
    }

    @ModelAttribute("masterList")
    public List<MasterDto> masterList(){
        return masterService.findAll();
    }

    @GetMapping
    public String showReceptionList(@RequestParam(name="master", required = false) String masterName,
                                    @RequestParam(name="client", required = false) String clientName,
                                    @RequestParam(name="room", required = false) String roomNumber,
                                    @RequestParam(name="dateOfVisit", required = false) LocalDate dateOfVisit,
                                    Model model){

        model.addAttribute("receptionList", receptionService.findAllWithFilter(
                masterName, clientName, roomNumber, dateOfVisit));

        model.addAttribute("filterMaster", masterName);
        model.addAttribute("filterClient", clientName);
        model.addAttribute("filterRoom", roomNumber);
        model.addAttribute("filterDate", dateOfVisit);

        return "reception/list-reception";
    }

    @GetMapping("create")
    public String getNewReceptionPage(Model model){
        LocalTime startTime = LocalTime.now();
        LocalTime finishTime = startTime.plusMinutes(15);
        model.addAttribute("reception", new ReceptionDto(
                null,
                null,
                null,
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(startTime.getHour(), startTime.getMinute()),
                LocalTime.of(finishTime.getHour(), finishTime.getMinute()),
                ""));

        return "reception/new_reception";
    }

    @GetMapping("/{recordId:\\d+}/online")
    public String getNewReceptionPageForRecordId(@PathVariable("recordId") Long recordId,
                                                 Model model){

        RecordDto onlineRecord = onlineRecordService.findRecordById(recordId).orElse(null);
        if (onlineRecord != null) {
            onlineRecordService.changeStatusForRecord(recordId, RecordStatus.IN_WORK);
            onlineRecord = RecordDto.builder()
                    .id(recordId)
                    .phone(onlineRecord.phone())
                    .name(onlineRecord.name())
                    .note(onlineRecord.note())
                    .status(RecordStatus.IN_WORK.name())
                    .build();
        }
        model.addAttribute("onlineRecord", onlineRecord);
        model.addAttribute("reception", new ReceptionDto(
                null,
                null,
                null,
                null,
                LocalDate.now().plusDays(1),
                LocalTime.now(),
                LocalTime.now().plusMinutes(15),
                ""));
        return "reception/new_reception";
    }

    @PostMapping("create")
    public String createReception(@RequestParam(name="online_record", required = false) Long recordId,
                                  @RequestParam(name="master", required = false) Long masterId,
                                  @RequestParam(name="client", required = false) Long clientId,
                                  @RequestParam(name="room", required = false) Long roomId,
                                  @RequestParam(name="dateOfVisit", required = false) LocalDate dateOfVisit,
                                  @RequestParam(name="startTime", required = false) LocalTime startTime,
                                  @RequestParam(name="finishTime", required = false) LocalTime finishTime,
                                  @RequestParam(name="details", required = false) String details,
                                  Model model){
        if(recordId != null){
            onlineRecordService.changeStatusForRecord(recordId, RecordStatus.DONE);
        }
        startTime = (startTime == null ? LocalTime.now() : startTime);
        finishTime = (finishTime == null ? startTime.plusMinutes(15) : finishTime);
        ReceptionDto newReception = ReceptionDto.builder()
                .master(masterService.findMasterById(masterId)
                        .orElseThrow(() -> new NotFoundDirectoryElementException("Указанный мастер не существует")))
                .client(clientService.findClientById(clientId)
                        .orElseThrow(() -> new NotFoundDirectoryElementException("Указанный клиент не существует")))
                .room(roomService.findRoomById(roomId)
                        .orElseThrow(() -> new NotFoundDirectoryElementException("Указанное помещение не существует")))
                .dateOfVisit(dateOfVisit)
                .startTime(startTime)
                .finishTime(finishTime)
                .details(details)
                .build();

        try {
            ReceptionDto reception = receptionService.createReception(
                    newReception.master(),
                    newReception.room(),
                    newReception.client(),
                    newReception.dateOfVisit(),
                    newReception.startTime(),
                    newReception.finishTime(),
                    newReception.details());
            return "redirect:/receptions/%d".formatted(reception.id());
        }  catch(BadRequestException exception){
            model.addAttribute("reception", newReception);
            model.addAttribute("errors", exception.getErrors());
            return "receptions/new_reception";
        }
    }
}