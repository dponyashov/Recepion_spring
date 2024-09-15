package ru.dponyashov.dto;

import lombok.*;
import ru.dponyashov.safety.annotation.StringToEncode;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@StringToEncode
public class ClientDto{
    private Long id;

    @StringToEncode
    private String name;
    @StringToEncode
    private String phone;
    @StringToEncode
    private String mail;
    private List<NotificationDto> notifications;
}
