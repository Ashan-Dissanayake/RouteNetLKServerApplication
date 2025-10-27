package lk.ashan.routenetlkserverapllication.module.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeRequestDto {

    private  String number;
    private  String fullname;
    private  String callingname;
    private  String nic;
    private  GenderDto gender;
    private  String mobile;
    private  String email;
    private  String address;
    private  String emergencycontact;
    private  byte[] image;
    private  LocalDate doj;
    private  BranchSummaryResponseDto branch;
    private  DepartmentDto department;
    private  DesignationDto designation;
    private  EmployeetypeDto employeetype;
    private  EmployeestatusDto employeestatus;
}
