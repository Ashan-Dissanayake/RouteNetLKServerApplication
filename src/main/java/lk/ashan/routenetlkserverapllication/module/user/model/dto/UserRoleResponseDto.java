package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponseDto {
    private Integer userId;
    private String username;
    private List<RoleDto> roles;
}
