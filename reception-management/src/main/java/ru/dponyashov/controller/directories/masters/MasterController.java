package ru.dponyashov.controller.directories.masters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.exception.NotFoundDirectoryElementException;
import ru.dponyashov.service.MasterService;

@Controller
@RequestMapping("/directories/masters/{masterId:\\d+}")
@RequiredArgsConstructor
public class MasterController {

    private final MasterService masterService;

    @ModelAttribute("master")
    public MasterDto master(@PathVariable("masterId") Long masterId){
        return masterService.findMasterById(masterId)
                .orElseThrow(() -> new NotFoundDirectoryElementException("Мастер не найден"));
    }

    @GetMapping
    public String getMasterPage(){
        return "directory/masters/master";
    }

    @GetMapping("edit")
    public String getMasterEditPage(){
        return "directory/masters/edit_master";
    }

    @PostMapping("edit")
    public String updateMaster(@ModelAttribute(value = "master", binding = false) MasterDto master,
                               MasterDto newMaster, Model model){
        try {
            masterService.updateMaster(master.getId(), newMaster.getName(), newMaster.getPhone());
            return "redirect:/directories/masters/%d".formatted(master.getId());
        } catch(BadRequestException exception){
            model.addAttribute("new_master", newMaster);
            model.addAttribute("errors", exception.getErrors());
            return "/directory/masters/edit_master";
        }
    }

    @GetMapping("delete")
    public String deleteMasterPage(@ModelAttribute("master") MasterDto master){
        return "directory/masters/delete_master";
    }

    @PostMapping("delete")
    public String deleteMaster(@ModelAttribute("master") MasterDto master){
        masterService.deleteMaster(master.getId());
        return "redirect:/directories/masters";
    }
}
