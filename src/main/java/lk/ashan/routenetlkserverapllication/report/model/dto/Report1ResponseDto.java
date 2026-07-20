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
public class Report1ResponseDto {
    // A single list of days guarantees alignment across all datasets
    private List<String> days;
    private List<Integer> successfulTrips;
    private List<Integer> breakdownCounts;
}
