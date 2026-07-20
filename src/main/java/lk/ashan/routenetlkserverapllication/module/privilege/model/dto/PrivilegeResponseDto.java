package lk.ashan.routenetlkserverapllication.module.privilege.model.dto;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.RoleDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeResponseDto {
    private Integer id;
    private String authority;
    private ModuleDto module;
    private OperationDto operation;
    private RoleDto role;
}
