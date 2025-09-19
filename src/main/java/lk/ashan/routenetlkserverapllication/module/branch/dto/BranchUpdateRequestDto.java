package lk.ashan.routenetlkserverapllication.module.branch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class BranchUpdateRequestDto extends BranchRequestDto {
    @NotNull
    private Integer id;
}
