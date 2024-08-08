package ru.dponyashov.dto;

import lombok.*;
import ru.dponyashov.enums.RecordStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecordFilter {
    private String phone;
    private String name;
    private RecordStatus status;
}