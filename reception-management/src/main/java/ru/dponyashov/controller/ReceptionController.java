package ru.dponyashov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reception")
public class ReceptionController {

    @GetMapping
    public String showReceptionList(){
        return "reception/list-reception";
    }
}