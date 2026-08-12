package lk.ashan.routenetlkserverapllication.module.privilege.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeAssignRequestDto {

    @NotEmpty(message = "At least one privilege must be selected")
    @Valid
    private List<PrivilegeRequestDto> privileges;

}
