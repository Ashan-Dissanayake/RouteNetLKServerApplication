package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class UserRequestDto {
    @NotNull(message = "Employee is mandatory")
    private EmployeeSummaryResponseDto employee;
    @Size(max = 45)
    @Pattern(regexp = "^([a-zA-Z0-9_.-]+)$", message = "Invalid Username")
    private String username;
    @Size(max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Invalid Password")
    private String password;
    @NotNull(message = "User Type is mandatory")
    private UserTypeDto usertype;
    @NotNull(message = "User Status is mandatory")
    private UserStatusDto userstatus;
    @NotNull(message = "Account Locked status is mandatory")
    private boolean accountlocked;
    @Pattern(regexp = "^.*$", message = "Invalid Description")
    private String remarks;
}
