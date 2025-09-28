package lk.ashan.routenetlkserverapllication.module.branch.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Collection;

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
    Collection<BranchDistrictCoverageDto> branchcoverages;

}

