package lk.ashan.routenetlkserverapllication.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report4ResponseDto {
    private List<String> logDates;
    private List<Long> totalPassengers;
    private List<Double> totalDistances;
}
