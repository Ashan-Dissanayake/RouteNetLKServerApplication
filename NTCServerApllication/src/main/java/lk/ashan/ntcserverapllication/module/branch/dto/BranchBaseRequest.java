package lk.ashan.ntcserverapllication.module.branch.dto;

import lk.ashan.ntcserverapllication.module.branch.model.Branchcoverage;
import lk.ashan.ntcserverapllication.module.branch.model.Branchstatus;
import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BranchBaseRequest {
    private String name;
    private String code;
    private String address;
    private String telephone;
    private String email;
    private Date docreated;
    private String remarks;
    private Branchtype branchtype;
    private Branchstatus branchstatus;
    private Collection<Branchcoverage> branchcoverages;
}
