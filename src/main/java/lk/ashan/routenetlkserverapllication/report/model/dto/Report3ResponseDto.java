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
public class Report3ResponseDto {
    private List<String> weeks;
    private List<Integer> completedServices;
    private List<Integer> pendingBacklog;
}
