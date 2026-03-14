package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class EmployeeValidationContext {

    private Integer id;

    private String number;
    private String nic;
    private String mobile;
    private String email;
    private String emergencyContact;

    private String departmentName;
    private String designationName;
    private String genderName;

    private String employeeTypeName;
    private LocalDate dateOfJoining;
}
