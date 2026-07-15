package lk.ashan.routenetlkserverapllication.module.privilege.model.dto;

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
}
