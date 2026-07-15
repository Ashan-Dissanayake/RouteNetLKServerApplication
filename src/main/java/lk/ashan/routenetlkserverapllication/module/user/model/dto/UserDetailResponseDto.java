package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDetailResponseDto {
    private Integer id;
    private EmployeeSummaryDto employee;
    private String username;
    private UserTypeDto usertype;
    private UserStatusDto userstatus;
    private boolean accountlocked;
    private String remarks;
}
