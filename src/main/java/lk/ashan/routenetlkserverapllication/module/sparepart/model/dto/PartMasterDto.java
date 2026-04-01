package lk.ashan.routenetlkserverapllication.module.sparepart.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartMasterDto {
    private Integer id;
    private String sku;
    private String name;
    private PartCategoryDto partcategory;
    private UnitOfMeasureDto unitofmeasure;
}
