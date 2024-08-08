package ru.dponyashov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.RecordDto;
import ru.dponyashov.dto.RecordFilter;
import ru.dponyashov.service.RecordService;

import java.util.List;

@RestController
@RequestMapping("/onlineRecord")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/list")
    public List<RecordDto> findAllWithFilter(RecordFilter filter){
        if(filter == null){
            return recordService.findAll();
        }
        return recordService.findWithFilter(filter);
    }

    @GetMapping("/{idRecord:\\d+}")
    public RecordDto findById(@PathVariable("idRecord") Long id){
        return recordService.findById(id);
    }

    @PostMapping
    public RecordDto saveRecord(@RequestBody RecordDto record){
        return recordService.save(record);
    }

    @PutMapping("/{idRecord:\\d+}")
    public RecordDto saveRecord(@PathVariable("idRecord") Long id,
                                @RequestBody RecordDto record){
        RecordDto recordFromDB = findById(id);
        recordFromDB.setName(record.getName() != null ? record.getName() : recordFromDB.getName());
        recordFromDB.setPhone(record.getPhone() != null ? record.getPhone() : recordFromDB.getPhone());
        recordFromDB.setNote(record.getNote() != null ? record.getNote() : recordFromDB.getNote());
        recordFromDB.setStatus(record.getStatus() != null ? record.getStatus() : recordFromDB.getStatus());
        return recordService.save(id, recordFromDB);
    }

    @DeleteMapping("/{idRecord:\\d+}")
    public void deleteRecord(@PathVariable("idRecord") Long id){
        recordService.delete(id);
    }
}
