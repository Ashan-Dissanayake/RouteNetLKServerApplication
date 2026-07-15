package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleAssignRequestDto {

    @NotEmpty(message = "At least one role must be selected")
    private List<RoleDto> roles;

}
