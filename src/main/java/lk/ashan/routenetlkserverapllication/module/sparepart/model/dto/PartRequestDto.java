package lk.ashan.routenetlkserverapllication.module.sparepart.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class PartRequestDto {
    @NotNull(message = "Branch is required")
    private BranchSummaryResponseDto branch;
    @NotNull(message = "Part category is required")
    private PartCategoryDto partcategory;
    @NotNull(message = "SKU is required")
    private String sku;
    @NotNull(message = "Name is required")
    private String name;
    private Byte[] photo;
    private String remarks;
    @NotNull(message = "QOH is required")
    private BigDecimal qoh;
    @NotNull(message = "Max level is required")
    private BigDecimal maxlevel;
    @NotNull(message = "ROP is required")
    private BigDecimal rop;
    @NotNull(message = "DO last ordered is required")
    private LocalDate dolastordered;
    @NotNull(message = "Unit of measure is required")
    private UnitOfMeasureDto unitofmeasure;
    @NotNull(message = "Part status is required")
    private PartStatusDto partstatus;
}
