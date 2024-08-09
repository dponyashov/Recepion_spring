package ru.dponyashov.controller.directories.masters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.MasterService;

@Controller
@RequestMapping("/directories/masters")
@RequiredArgsConstructor
public class MasterListController {

    private final MasterService masterService;

    @GetMapping
    public String showMasterList(Model model,
                                 @RequestParam(name = "masterName", required = false)String filterName,
                                 @RequestParam(name = "masterPhone", required = false)String filterPhone){
        model.addAttribute("masters", masterService.findAllWithFilter(filterName, filterPhone));
        model.addAttribute("filterName", filterName);
        model.addAttribute("filterPhone", filterPhone);

        return "directory/masters/master-list";
    }

    @GetMapping("create")
    public String getNewMasterPage(){
        return "directory/masters/new_master";
    }

    @PostMapping("create")
    public String createMaster(MasterDto newMaster, Model model){
        try {
            MasterDto master = masterService.createMaster(newMaster.name(), newMaster.phone());
            return "redirect:/directories/masters/%d".formatted(master.id());
        }  catch(BadRequestException exception){
            model.addAttribute("master", newMaster);
            model.addAttribute("errors", exception.getErrors());
            return "directory/masters/new_master";
        }
    }
}
