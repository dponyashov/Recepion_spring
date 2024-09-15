package ru.dponyashov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.entity.Notification;
import ru.dponyashov.mappers.Mapper;
import ru.dponyashov.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final ClientService clientService;

    @GetMapping
    List<NotificationDto> findAll(){
        return clientService.findAllNotification();
    }

    @GetMapping("/{notifyId:\\d+}")
    NotificationDto findById(@PathVariable("notifyId") Long notifyId){
        return clientService.findNotifyById(notifyId);
    }
}
