package ru.dponyashov.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dponyashov.safety.annotation.StringToEncode;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@StringToEncode
public class FilterReception {
        @StringToEncode
        private String clientName;
        @StringToEncode
        private String masterName;
        private String roomNumber;
        private LocalDate dateOfVisit;
        private Long idClient;
        private Long idMaster;
        private Long idRoom;
        private int pageSize;
        private int pageNumber;
}
