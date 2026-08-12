package lk.ashan.routenetlkserverapllication.module.privilege.model.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeRequestDto {

    @NotNull(message = "Module is mandatory")
    private ModuleDto module;

    @NotNull(message = "Operation is mandatory")
    private OperationDto operation;

}
