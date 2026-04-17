package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PrivilegeDto {
    private Integer id;
    private String authority;
    private RoleDto role;
    private ModuleDto module;
    private OperationDto operation;
}
