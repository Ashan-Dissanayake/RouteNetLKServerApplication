package lk.ashan.routenetlkserverapllication.module.grn.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class GrnRequestDto {
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;
    @NotNull(message = "Part is mandatory")
    private PartRequestSummaryResponseDto partrequest;
    @NotNull(message = "Number is mandatory")
    private String number;
    @NotNull(message = "Received is mandatory")
    private LocalDate doreceived;
    private String remarks;
    @NotNull(message = "Status is mandatory")
    private GrnStatusDto grnstatus;
    @NotNull(message = "GRN parts are mandatory")
    private List<GrnPartDto> grnparts;
}
