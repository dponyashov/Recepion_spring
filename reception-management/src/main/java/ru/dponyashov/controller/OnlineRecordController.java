package ru.dponyashov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/online-records")
public class OnlineRecordController {

    @GetMapping
    public String showOnlineRecordsList(){

        return "online-record/list-records";
    }
}
