package lk.ashan.routenetlkserverapllication.module.farecollection.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FareCollectionCreateRequestDto {
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;
    @NotNull(message = "Trip Execution is mandatory")
    private TripExecutionSummaryDto tripexecution;
    @NotNull(message = "Ticket Machine is mandatory")
    private TicketMachineDto ticketmachine;
    @NotNull(message = "Total Ticket Amount is mandatory")
    private Integer totaltickets;
    @NotNull(message = "Cache Collected Amount is mandatory")
    private BigDecimal cachecollected;
    @NotNull(message = "Digital Payment Amount is mandatory")
    private BigDecimal digitalpayments;
}
