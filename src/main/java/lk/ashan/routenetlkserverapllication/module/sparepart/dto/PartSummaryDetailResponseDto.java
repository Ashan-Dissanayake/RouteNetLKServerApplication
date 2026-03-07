package lk.ashan.routenetlkserverapllication.module.sparepart.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartSummaryDetailResponseDto {
    private Integer id;
    private String name;

}
