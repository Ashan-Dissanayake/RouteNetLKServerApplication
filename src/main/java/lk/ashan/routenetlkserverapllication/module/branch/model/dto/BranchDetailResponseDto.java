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
    private Integer id;
    private String name;
    private String code;
    private String address;
    private String telephone;
    private String email;
    private LocalDate docreated;
    private String remarks;
    private BranchTypeDto branchtype;
    private BranchStatusDto branchstatus;
    private RegionalOfficeDto regionaloffice;

}

