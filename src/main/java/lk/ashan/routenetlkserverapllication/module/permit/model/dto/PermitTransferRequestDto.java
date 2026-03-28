package lk.ashan.routenetlkserverapllication.module.permit.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitTransferRequestDto {
    private Integer newBranchId; // target branch/depot
    private Integer newStatusId; // e.g., TRANSFERRED
    private String remarks;   // optional comment
}
