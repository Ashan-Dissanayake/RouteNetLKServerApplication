package lk.ashan.routenetlkserverapllication.module.sparepart.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data Transfer Object for creating a new spare part")
public class PartCreateRequestDto{
    @NotNull(message = "Branch is required")
    @Schema(description = "The branch where this part is located")
    private BranchSummaryDto branch;
    
    @Pattern( regexp = "^[A-Za-z0-9 ,./()%-]{0,255}$",message = "Remarks contains invalid characters")
    @Schema(description = "Additional remarks about the part", example = "Initial stock")
    private String remarks;
    
    @NotNull(message = "QOH is required")
    @DecimalMin(value = "0", message = "QOH cannot be negative")
    @Schema(description = "Quantity On Hand", example = "50")
    private BigDecimal qoh;
    
    @NotNull(message = "Max level is required")
    @DecimalMin(value = "1", message = "Max level must be greater than 0")
    @Schema(description = "Maximum inventory level", example = "100")
    private BigDecimal maxlevel;
    
    @NotNull(message = "ROP is required")
    @DecimalMin(value = "1", message = "ROP must be greater than 0")
    @Schema(description = "Reorder Point", example = "20")
    private BigDecimal rop;
    
    // @NotNull(message = "DO last ordered is required")
    @PastOrPresent(message = "Date cannot be in the future")
    @Schema(description = "Date the part was last ordered")
    private LocalDate dolastordered;
    
    @NotNull(message = "Part status is required")
    @Schema(description = "Current status of the part")
    private PartStatusDto partstatus;
    
    @NotNull(message = "Part Master is required")
    @Schema(description = "Reference to the part master definition")
    private PartMasterDto partmaster;

}
