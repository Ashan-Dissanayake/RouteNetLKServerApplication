package lk.ashan.routenetlkserverapllication.module.employee.model.dto;

import jakarta.validation.constraints.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class EmployeeRequestDto {

//    @NotBlank(message = "Number is mandatory")
//    @Pattern(regexp = "^EMP\\d{4}$", message = "Invalid employee number")
//    private  String number;

    @Pattern(regexp = "^([A-Z][a-z]*[.]?[\\s]?)*([A-Z][a-z]*)$", message = "Invalid full name")
    @NotBlank(message = "Full name is mandatory")
    private  String fullname;

    @NotBlank(message = "Calling name is mandatory")
    @Pattern(regexp = "^([A-Z][a-z]+)$", message = "Invalid calling name")
    private  String callingname;

    @NotBlank(message = "NIC is mandatory")
    @Pattern(regexp = "^(([\\d]{9}[vVxX])|([\\d]{12}))$", message = "Invalid NIC")
    private  String nic;

    @NotNull(message = "Gender is mandatory")
    private  GenderDto gender;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "^(070|071|072|074|075|076|077|078)\\d{7}$",message = "Invalid mobile number")
    private  String mobile;

//    @NotBlank(message = "Email is mandatory")
//    @Email(message = "Invalid email format")
//    private  String email;

    @NotBlank(message = "Address is mandatory")
    @Pattern(regexp = "^([\\w/\\-,\\s]{2,})$", message = "Invalid address")
    private  String address;

    @NotBlank(message = "Emergency contact is mandatory")
    @Pattern(regexp = "^0(?:(7[0|1|2|4-8])|(1[1]|2[1-7]|3[1-8]|4[1|5|7]|5[1|2|4|5|7]|6[3|5|6|7]|8[1]|9[1]))\\d{7}$",
            message = "Invalid emergency contact")
    private  String emergencycontact;

    private  byte[] image;

    @NotNull(message = "Date of joining is mandatory")
    @PastOrPresent(message = "Date of joining cannot be in the future")
    private LocalDate doj;

    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;

    @NotNull(message = "Department is mandatory")
    private  DepartmentDto department;

    @NotNull(message = "Designation is mandatory")
    private  DesignationDto designation;

    @NotNull(message = "Employee type is mandatory")
    private EmployeeTypeDto employeetype;

    @NotNull(message = "Employee status is mandatory")
    private EmployeeStatusDto employeestatus;
}
