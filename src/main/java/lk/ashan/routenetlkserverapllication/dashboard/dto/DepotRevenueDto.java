package lk.ashan.routenetlkserverapllication.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepotRevenueDto {
    private long totalTickets;
    private BigDecimal cashCollected;
    private BigDecimal digitalPayments;
    private boolean isReconciled;
}
