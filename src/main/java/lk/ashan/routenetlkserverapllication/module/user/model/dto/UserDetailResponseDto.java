package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryResponseDto;
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
    private EmployeeSummaryResponseDto employee;
    private String username;
    private String password;
    private UserTypeDto usertype;
    private UserStatusDto userstatus;
    private boolean accountlocked;
    private String recoverycode;
    private Timestamp recoverycodeexpiration;
    private boolean recoverycodeused;
    private List<UserRoleDto> userRoles;
    private String remarks;
}
