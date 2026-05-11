package lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TripExecutionInitializationDto {
    private Branch branch;
    private LocalDate doservice;
}
