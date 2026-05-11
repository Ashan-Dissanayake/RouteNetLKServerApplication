package lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TripExecutionAssignmentDto {
    private Integer branchId;
    private LocalDate date;
}
