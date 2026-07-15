package lk.ashan.routenetlkserverapllication.module.privilege.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePrivilegeResponseDto {
    private Integer roleId;
    private String roleName;
    private List<PrivilegeResponseDto> privileges;

}
