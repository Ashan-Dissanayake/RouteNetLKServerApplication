package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchContext {
    private Integer id;
    private String code;
    private String name;
    private String email;
    private String telephone;
    private String address;
    private Integer branchStatusId;
}
