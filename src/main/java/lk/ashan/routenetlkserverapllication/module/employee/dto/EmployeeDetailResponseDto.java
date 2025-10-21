package lk.ashan.routenetlkserverapllication.module.employee.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeDetailResponseDto implements Serializable {
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
    private  Boolean deleted;
    private  GenderDto gender;
    private  BranchSummaryResponseDto branch;
    private  DepartmentDto department;
    private  DesignationDto designation;
    private  EmployeetypeDto employeetype;
    private  EmployeestatusDto employeestatus;
}
