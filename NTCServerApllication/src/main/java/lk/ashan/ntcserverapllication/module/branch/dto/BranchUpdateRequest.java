package lk.ashan.ntcserverapllication.module.branch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BranchUpdateRequest extends BranchBaseRequest{
    private Integer id;
}
