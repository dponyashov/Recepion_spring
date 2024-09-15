package ru.dponyashov.dto;

import lombok.*;
import ru.dponyashov.safety.annotation.StringToEncode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@StringToEncode
public class MasterDto {
    private Long id;
    @StringToEncode
    private String name;
    @StringToEncode
    private String phone;
}
