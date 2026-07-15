package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    @NotNull
    private Integer id;

    @NotNull(message = "Employee is mandatory")
    private EmployeeSummaryDto employee;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 45)
    @Pattern(regexp = "^([a-zA-Z0-9_.-]+)$", message = "Invalid Username")
    private String username;

    @NotNull(message = "User Type is mandatory")
    private UserTypeDto usertype;

    @Pattern(regexp = "^.*$", message = "Invalid Description")
    private String remarks;
}
