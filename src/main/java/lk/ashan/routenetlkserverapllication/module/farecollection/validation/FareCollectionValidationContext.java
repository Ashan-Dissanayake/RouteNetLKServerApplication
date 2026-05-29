package lk.ashan.routenetlkserverapllication.module.farecollection.validation;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class FareCollectionValidationContext {
    private final Integer branchId;
    private final Integer tripExecutionId;
    private final Integer ticketMachineId;
    private final Integer totalTickets;
    private final BigDecimal cashCollected;
    private final BigDecimal digitalPayments;
}
