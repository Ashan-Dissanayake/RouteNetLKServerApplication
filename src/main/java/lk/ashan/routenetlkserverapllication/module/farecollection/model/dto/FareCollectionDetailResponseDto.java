package lk.ashan.routenetlkserverapllication.module.farecollection.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FareCollectionDetailResponseDto {
    private Integer id;
    private BranchSummaryDto branch;
    private TripExecutionSummaryDto tripexecution;
    private TicketMachineDto ticketmachine;
    private Integer totaltickets;
    private BigDecimal cachecollected;
    private BigDecimal digitalpayments;
    private Boolean isreconciled;
    private LocalTime tocollected;
}
