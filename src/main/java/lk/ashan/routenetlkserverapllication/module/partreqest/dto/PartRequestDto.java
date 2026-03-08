package lk.ashan.routenetlkserverapllication.module.partreqest.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PartRequestDto {
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryResponseDto branch;
    @NotNull(message = "Number is mandatory")
    private String number;
    @NotNull(message = "Requested date is mandatory")
    private LocalDate dorequested;
    private String remarks;
    @NotNull(message = "Status is mandatory")
    private PartRequestStatusDto partrequeststatus;
    @NotNull(message = "Request items are mandatory")
    private List<PartRequestItemDto> partrequestitems;
}
