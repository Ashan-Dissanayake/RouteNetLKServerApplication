package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShiftSummaryDto {
    private Integer id;
    private String name;
    private LocalTime tostart;
    private LocalTime toend;
}
