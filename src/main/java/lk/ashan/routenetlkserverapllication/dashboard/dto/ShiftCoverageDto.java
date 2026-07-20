package lk.ashan.routenetlkserverapllication.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftCoverageDto {
    private String shiftName;
    private String timeRange; // e.g., "04:30 AM - 12:30 PM"
    private int requiredCount;
    private int assignedCount;
    private String status; // "FULLY_STAFFED" or "UNDERSTAFFED"
}
