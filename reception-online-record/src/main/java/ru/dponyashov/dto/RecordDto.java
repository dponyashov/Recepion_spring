package ru.dponyashov.dto;

import lombok.*;
import ru.dponyashov.enums.RecordStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecordDto {
    private Long id;
    private String phone;
    private String name;
    private String note;
    private RecordStatus status;

    public void clear() {
        this.id = null;
        this.phone = "";
        this.name = "";
        this.note = "";
        this.status = RecordStatus.NEW;
    }
}