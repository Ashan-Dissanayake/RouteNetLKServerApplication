package lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TripExecutionAssignmentDto {
    private Integer branchId;
    private LocalDate date;
}
