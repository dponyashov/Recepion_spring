package ru.dponyashov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dponyashov.entity.Room;
import ru.dponyashov.service.RoomService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> findAll(@RequestParam(name="number", required = false) String numberFilter){
        return new ResponseEntity<>(roomService.findByNumber(numberFilter), HttpStatus.OK);
    }

    @GetMapping("/{roomId:\\d+}")
    public ResponseEntity<Room> findById(@PathVariable("roomId") Long id){
        Room roomFromDB = roomService.findById(id);
        return new ResponseEntity<>(roomFromDB, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Room> saveRoom(@Valid @RequestBody Room newRoom,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Room roomFromDB = roomService.save(newRoom);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .path("/api/room/{roomId}")
                            .build(Map.of("roomId", roomFromDB.getId())))
                    .body(roomFromDB);
        }
    }

    @PutMapping("/{roomId:\\d+}")
    public ResponseEntity<Room> saveRoom(@PathVariable("roomId") Long id,
                                         @Valid @RequestBody Room room,
                                         BindingResult bindingResult) throws BindException{
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            room.setId(id);
            Room roomFromDB = roomService.save(room);
            return new ResponseEntity<>(roomFromDB, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{roomId:\\d+}")
    public ResponseEntity<Void> deleteMaster(@PathVariable("roomId") Long id){
        roomService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
