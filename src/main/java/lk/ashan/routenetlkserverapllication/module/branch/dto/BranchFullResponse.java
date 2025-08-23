package lk.ashan.routenetlkserverapllication.module.branch.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branchcoverage;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchtype;
import lombok.Value;

import java.sql.Date;
import java.util.Collection;


@Value
public class BranchFullResponse {
    Integer id;
    String name;
    String code;
    String address;
    String telephone;
    String email;
    Date docreated;
    String remarks;
    Branchtype branchtype;
    Branchstatus branchstatus;
    Collection<Branchcoverage> branchcoverages;

}

