package lk.ashan.routenetlkserverapllication.module.branch.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BranchUpdateRequestDto extends BranchRequestDto {
    @NotNull
    private Integer id;
}
