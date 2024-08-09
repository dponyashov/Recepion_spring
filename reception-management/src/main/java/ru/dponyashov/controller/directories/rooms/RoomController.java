package ru.dponyashov.controller.directories.rooms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.exception.NoFoundDirectoryElementException;
import ru.dponyashov.service.RoomService;

@Controller
@RequestMapping("/directories/rooms/{roomId:\\d+}")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @ModelAttribute("room")
    public RoomDto room(@PathVariable("roomId") Long roomId){
        return roomService.findRoomById(roomId)
                .orElseThrow(() -> new NoFoundDirectoryElementException("Помещение не найдено"));
    }

    @GetMapping
    public String getRoomPage(){
        return "directory/rooms/room";
    }

    @GetMapping("edit")
    public String getRoomEditPage(){
        return "directory/rooms/edit_room";
    }

    @PostMapping("edit")
    public String updateRoom(@ModelAttribute(value = "room", binding = false) RoomDto room,
                               RoomDto newRoom, Model model){
        try {
            roomService.updateRoom(room.id(), newRoom.number(), newRoom.note());
            return "redirect:/directories/rooms/%d".formatted(room.id());
        } catch(BadRequestException exception){
            model.addAttribute("new_room", newRoom);
            model.addAttribute("errors", exception.getErrors());
            return "/directory/rooms/edit_room";
        }
    }

    @PostMapping("delete")
    public String deleteRoom(@ModelAttribute("room") RoomDto room){
        roomService.deleteMaster(room.id());
        return "redirect:/directories/rooms";
    }
}
