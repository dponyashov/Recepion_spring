package ru.dponyashov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dponyashov.dto.ReceptionDto;
import ru.dponyashov.dto.filter.FilterReception;
import ru.dponyashov.entity.Reception;
import ru.dponyashov.service.ReceptionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reception")
@RequiredArgsConstructor
public class ReceptionController {

    private final ReceptionService receptionService;

    @GetMapping
    public ResponseEntity<List<ReceptionDto>> findReception(FilterReception filterReception){
        if(filterReception != null){
            return new ResponseEntity<>(receptionService.findWithFilter(filterReception), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(receptionService.findAll(), HttpStatus.OK);
        }
    }

    @GetMapping("/{idReception:\\d+}")
    public ResponseEntity<ReceptionDto> findById(@PathVariable("idReception") Long id){
        ReceptionDto receptionFromDB = receptionService.findById(id);
        return new ResponseEntity<>(receptionFromDB, HttpStatus.OK);
    }

    @GetMapping("/{idReception:\\d+}/notify")
    public ResponseEntity<Void> sendNotify(@PathVariable("idReception") Long id){
        receptionService.sendNotifications(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReceptionDto> saveReception(@Valid @RequestBody ReceptionDto newReception,
                                                   BindingResult bindingResult,
                                                   UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            ReceptionDto receptionFromDB = receptionService.save(newReception);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .path("/api/reception/{receptionId}")
                            .build(Map.of("receptionId", receptionFromDB.id())))
                    .body(receptionFromDB);
        }
    }

    @PutMapping("/{idReception:\\d+}")
    public ResponseEntity<ReceptionDto> saveReception(@PathVariable("idReception") Long id,
                                                   @Valid @RequestBody ReceptionDto reception,
                                                   BindingResult bindingResult) throws BindException{
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            ReceptionDto receptionForSave = ReceptionDto.builder()
                    .id(id)
                    .client(reception.client())
                    .master(reception.master())
                    .room(reception.room())
                    .dateOfVisit(reception.dateOfVisit())
                    .startTime(reception.startTime())
                    .finishTime(reception.finishTime())
                    .details(reception.details())
                    .build();
            ReceptionDto receptionFromDB = receptionService.save(receptionForSave);
            return new ResponseEntity<>(receptionFromDB, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idReception:\\d+}")
    public ResponseEntity<Void> deleteReception(@PathVariable("idReception") Long id){
        receptionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}