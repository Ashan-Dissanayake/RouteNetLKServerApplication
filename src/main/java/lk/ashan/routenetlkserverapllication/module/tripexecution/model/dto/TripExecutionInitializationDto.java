package lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TripExecutionInitializationDto {
    private Branch branch;
    private LocalDate doservice;
}
