package ru.dponyashov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.enums.RecordStatus;
import ru.dponyashov.service.OnlineRecordService;

@Controller
@RequestMapping("/online-records")
@RequiredArgsConstructor
public class OnlineRecordController {

    private final OnlineRecordService recordService;

    @GetMapping
    public String showOnlineRecordsList(Model model,
                                        @RequestParam(name = "name", required = false)String filterName,
                                        @RequestParam(name = "phone", required = false)String filterPhone){

        model.addAttribute("recordList", recordService.findNewRecordWithFilter(filterName, filterPhone));
        model.addAttribute("filterName", filterName);
        model.addAttribute("filterPhone", filterPhone);

        return "online-record/list-records";
    }

    @PostMapping("/{recordId:\\d+}/cancel")
    public String cancelOnlineRecord(@PathVariable("recordId") Long recordId){

        recordService.changeStatusForRecord(recordId, RecordStatus.CANCELLED);

        return "redirect:/online-records";
    }
}