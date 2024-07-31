package ru.dponyashov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dponyashov.dto.filter.FilterMaster;
import ru.dponyashov.entity.Master;
import ru.dponyashov.service.MasterService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/master")
@RequiredArgsConstructor
public class MasterController {
    private final MasterService masterService;

    @GetMapping
    public ResponseEntity<List<Master>> findClient(FilterMaster filterMaster){
        if(filterMaster != null){
            return new ResponseEntity<>(masterService.findWithFilter(filterMaster), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(masterService.findAll(), HttpStatus.OK);
        }
    }

    @GetMapping("/{idMaster:\\d+}")
    public ResponseEntity<Master> findById(@PathVariable("idMaster") Long id){
        Master masterFromDB =  masterService.findById(id);
        return new ResponseEntity<>(masterFromDB, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Master> saveMaster(@Valid @RequestBody Master newMaster,
                                             BindingResult bindingResult,
                                             UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Master masterFromDB = masterService.save(newMaster);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .path("/api/room/{masterId}")
                            .build(Map.of("masterId", newMaster.getId())))
                    .body(masterFromDB);
//            return new ResponseEntity<>(masterFromDB, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{idMaster:\\d+}")
    public ResponseEntity<Master> saveMaster(@PathVariable("idMaster") Long id,
                                             @Valid @RequestBody Master master,
                                             BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()){
            if(bindingResult instanceof BindException exception){
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            master.setId(id);
            Master masterFromDB = masterService.save(master);
            return new ResponseEntity<>(masterFromDB, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idMaster:\\d+}")
    public ResponseEntity<Void> deleteMaster(@PathVariable("idMaster") Long id){
        masterService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
