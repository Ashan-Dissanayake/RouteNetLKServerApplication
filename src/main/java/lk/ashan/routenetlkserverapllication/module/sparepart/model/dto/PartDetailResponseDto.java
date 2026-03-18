package lk.ashan.routenetlkserverapllication.module.sparepart.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartDetailResponseDto {
    private Integer id;
    private BranchSummaryDto branch;
    private PartCategoryDto partcategory;
    private String sku;
    private String name;
    private Byte[] photo;
    private String remarks;
    private BigDecimal qoh;
    private BigDecimal maxlevel;
    private BigDecimal rop;
    private LocalDate dolastordered;
    private UnitOfMeasureDto unitofmeasure;
    private PartStatusDto partstatus;
}
