package ru.dponyashov.controller.directories.rooms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.RoomService;

@Controller
@RequestMapping("/directories/rooms")
@RequiredArgsConstructor
public class RoomListController {

    private final RoomService roomService;


    @GetMapping
    public String showRoomList(Model model,
                               @RequestParam(name = "filterNumber", required = false)String filterNumber){
        model.addAttribute("rooms", roomService.findAllWithFilter(filterNumber));
        model.addAttribute("filterNumber", filterNumber);
        return "directory/rooms/room-list";
    }

    @GetMapping("create")
    public String getNewRoomPage(){
        return "directory/rooms/new_room";
    }

    @PostMapping("create")
    public String createRoom(RoomDto newRoom, Model model){
        try {
            RoomDto room = roomService.createRoom(newRoom.number(), newRoom.note());
            return "redirect:/directories/rooms/%d".formatted(room.id());
        }  catch(BadRequestException exception){
            model.addAttribute("room", newRoom);
            model.addAttribute("errors", exception.getErrors());
            return "directory/rooms/new_room";
        }
    }
}
