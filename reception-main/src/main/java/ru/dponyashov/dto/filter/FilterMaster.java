package ru.dponyashov.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dponyashov.safety.annotation.StringToEncode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@StringToEncode
public class FilterMaster {
    @StringToEncode
    private String masterName;
    @StringToEncode
    private String masterPhone;
    private int pageSize;
    private int pageNumber;
}
