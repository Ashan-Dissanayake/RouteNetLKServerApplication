package lk.ashan.routenetlkserverapllication.module.partreqest.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
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
public class PartRequestDto {
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;
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
