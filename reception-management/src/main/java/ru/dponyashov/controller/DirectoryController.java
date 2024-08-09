package ru.dponyashov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/directories")
public class DirectoryController {

    @GetMapping
    public String showDirectories(){
        return "directory/list-directories";
    }
}
