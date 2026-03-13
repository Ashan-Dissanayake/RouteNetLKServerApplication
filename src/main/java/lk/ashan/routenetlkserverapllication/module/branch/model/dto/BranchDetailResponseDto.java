package lk.ashan.routenetlkserverapllication.module.branch.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BranchDetailResponseDto {
    Integer id;
    String name;
    String code;
    String address;
    String telephone;
    String email;
    LocalDate docreated;
    String remarks;
    BranchtypeDto branchtype;
    BranchstatusDto branchstatus;
    RegionalOfficeDto regionaloffice;

}

