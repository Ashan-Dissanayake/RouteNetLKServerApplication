package lk.ashan.routenetlkserverapllication.module.farecollection.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TicketMachineDto {
    private Integer id;
    private String name;
    private BranchSummaryDto branch;
}
