package lk.ashan.routenetlkserverapllication.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report2ResponseDto {
    private List<String> depots;
    private List<BigDecimal> cashAmounts;
    private List<BigDecimal> digitalAmounts;
}
