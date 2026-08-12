package lk.ashan.routenetlkserverapllication.module.sparepart.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUpdateRequestDto{
    @NotNull(message = "ID is required")
    private Integer id;
    @NotNull(message = "Branch is required")
    private BranchSummaryDto branch;
    @Pattern( regexp = "^[A-Za-z0-9 ,./()%-]{0,255}$",message = "Remarks contains invalid characters")
    private String remarks;
    @NotNull(message = "QOH is required")
    @DecimalMin(value = "0", message = "QOH cannot be negative")
    private BigDecimal qoh;
    @NotNull(message = "Max level is required")
    @DecimalMin(value = "1", message = "Max level must be greater than 0")
    private BigDecimal maxlevel;
    @NotNull(message = "ROP is required")
    @DecimalMin(value = "1", message = "ROP must be greater than 0")
    private BigDecimal rop;
    // @NotNull(message = "DO last ordered is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate dolastordered;
    @NotNull(message = "Part status is required")
    private PartStatusDto partstatus;
    @NotNull(message = "Part Master is required")
    private PartMasterDto partmaster;

}
