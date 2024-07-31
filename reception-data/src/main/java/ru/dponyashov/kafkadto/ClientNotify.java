package ru.dponyashov.kafkadto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientNotify {
    private Long idReception;
    private String dateOfVisit;
    private String startTime;
    private String nameClient;
    private String phoneClient;
    private String mailClient;
}
