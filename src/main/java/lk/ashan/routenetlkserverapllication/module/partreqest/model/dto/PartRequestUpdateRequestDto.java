package lk.ashan.routenetlkserverapllication.module.partreqest.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartRequestUpdateRequestDto{

    @NotNull(message = "Id is mandatory")
    private Integer id;
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;
    private String number;
    @NotNull(message = "Requested date is mandatory")
    @PastOrPresent(message = "Requested date cannot be in the future")
    private LocalDate dorequested;
    private String remarks;
    @NotNull(message = "Status is mandatory")
    private PartRequestStatusDto partrequeststatus;
    @NotNull(message = "Request items are mandatory")
    private List<PartRequestItemDto> partrequestitems;
}
