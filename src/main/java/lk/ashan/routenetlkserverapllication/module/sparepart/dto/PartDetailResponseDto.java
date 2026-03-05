package lk.ashan.routenetlkserverapllication.module.sparepart.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PartDetailResponseDto {
    private BranchSummaryResponseDto branch;
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
