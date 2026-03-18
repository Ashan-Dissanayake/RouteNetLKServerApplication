package lk.ashan.routenetlkserverapllication.module.employee.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
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
    private String callingname;
    private  String nic;
    private  String mobile;
    private  String email;
    private  String address;
    private  String emergencycontact;
    private  byte[] image;
    private  LocalDate doj;
    private  GenderDto gender;
    private BranchSummaryDto branch;
    private  DepartmentDto department;
    private  DesignationDto designation;
    private EmployeeTypeDto employeetype;
    private EmployeeStatusDto employeestatus;
}
