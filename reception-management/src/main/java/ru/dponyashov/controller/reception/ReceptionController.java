package ru.dponyashov.controller.reception;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.dto.ReceptionDto;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.exception.NotFoundDirectoryElementException;
import ru.dponyashov.exception.NotFoundReception;
import ru.dponyashov.service.ClientService;
import ru.dponyashov.service.MasterService;
import ru.dponyashov.service.ReceptionService;
import ru.dponyashov.service.RoomService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/receptions/{receptionId:\\d+}")
@RequiredArgsConstructor
public class ReceptionController {

    private final ReceptionService receptionService;
    private final ClientService clientService;
    private final MasterService masterService;
    private final RoomService roomService;

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

    @ModelAttribute("reception")
    public ReceptionDto reception(@PathVariable("receptionId") Long receptionId){
        return receptionService.findReceptionById(receptionId)
                .orElseThrow(NotFoundReception::new);
    }

    @GetMapping
    public String getReceptionPage(){
        return "reception/reception";
    }

    @GetMapping("/edit")
    public String getReceptionEditPage(){
        return "reception/edit_reception";
    }

    @PostMapping("edit")
    public String updateReception(@ModelAttribute(value = "reception", binding = false) ReceptionDto reception,
                                  @RequestParam(name="master", required = false) Long masterId,
                                  @RequestParam(name="client", required = false) Long clientId,
                                  @RequestParam(name="room", required = false) Long roomId,
                                  @RequestParam(name="dateOfVisit", required = false) LocalDate dateOfVisit,
                                  @RequestParam(name="startTime", required = false) LocalTime startTime,
                                  @RequestParam(name="finishTime", required = false) LocalTime finishTime,
                                  @RequestParam(name="details", required = false) String details,
                               Model model){

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
            receptionService.updateReception(reception.id(),
                    newReception.master(),
                    newReception.room(),
                    newReception.client(),
                    newReception.dateOfVisit(),
                    newReception.startTime(),
                    newReception.finishTime(),
                    newReception.details());
            return "redirect:/receptions/%d".formatted(reception.id());
        } catch(BadRequestException exception){
            model.addAttribute("new_reception", newReception);
            model.addAttribute("errors", exception.getErrors());
            return "/receptions/edit_reception";
        }
    }

    @PostMapping("delete")
    public String deleteReception(@ModelAttribute("reception") ReceptionDto reception){
        receptionService.deleteReception(reception.id());
        return "redirect:/receptions";
    }
}