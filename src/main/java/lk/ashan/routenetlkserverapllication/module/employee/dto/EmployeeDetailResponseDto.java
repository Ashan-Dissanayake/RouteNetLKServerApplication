package lk.ashan.routenetlkserverapllication.module.employee.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeDetailResponseDto{
    private  Integer id;
    private  String number;
    private  String fullname;
    private  String nic;
    private  String mobile;
    private  String email;
    private  String address;
    private  String emergencycontact;
    private  byte[] image;
    private  LocalDate doj;
    private  GenderDto gender;
    private  BranchSummaryResponseDto branch;
    private  DepartmentDto department;
    private  DesignationDto designation;
    private  EmployeetypeDto employeetype;
    private  EmployeestatusDto employeestatus;
}
