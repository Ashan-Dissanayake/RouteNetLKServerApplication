package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActiveDeactiveDto {
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotNull(message = "Account lock status is mandatory")
    private Boolean accountLocked;
}
