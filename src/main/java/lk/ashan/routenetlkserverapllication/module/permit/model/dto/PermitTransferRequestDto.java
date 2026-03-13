package lk.ashan.routenetlkserverapllication.module.permit.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PermitTransferRequestDto {
    private Integer newBranchId; // target branch/depot
    private Integer newStatusId; // e.g., TRANSFERRED
    private String remarks;   // optional comment
}
