 package lk.ashan.routenetlkserverapllication.module.employee.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeSummaryDto {
    Integer id;
    String callingname;
    BranchSummaryDto branch;
}

