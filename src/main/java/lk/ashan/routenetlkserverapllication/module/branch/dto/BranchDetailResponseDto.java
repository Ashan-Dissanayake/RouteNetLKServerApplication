package lk.ashan.routenetlkserverapllication.module.branch.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branchcoverage;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchtype;
import lombok.*;

import java.sql.Date;
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
    Branchtype branchtype;
    Branchstatus branchstatus;
    Collection<Branchcoverage> branchcoverages;

}

