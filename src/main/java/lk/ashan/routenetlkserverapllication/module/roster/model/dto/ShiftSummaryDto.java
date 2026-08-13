package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftSummaryDto {
    private Integer id;
    private String name;
    private LocalTime tostart;
    private LocalTime toend;
    private String shiftFullName;
}
