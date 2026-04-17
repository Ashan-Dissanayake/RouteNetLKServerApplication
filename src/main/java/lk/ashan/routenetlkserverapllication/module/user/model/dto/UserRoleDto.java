package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRoleDto {
    private Integer id;
    private RoleDto role;
}
