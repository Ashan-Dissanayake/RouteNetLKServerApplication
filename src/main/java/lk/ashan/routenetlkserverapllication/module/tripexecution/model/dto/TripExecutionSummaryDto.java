package lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripExecutionSummaryDto {
    private Integer id;
    private String name;
}
