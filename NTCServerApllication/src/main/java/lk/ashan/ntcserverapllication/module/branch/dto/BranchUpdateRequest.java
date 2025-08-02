package lk.ashan.ntcserverapllication.module.branch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class BranchUpdateRequest extends BranchBaseRequest{
    @NotNull
    private Integer id;
}
