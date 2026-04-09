package lk.ashan.routenetlkserverapllication.module.grn.validation;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnPartRequestItem;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnPartRequestItemRepository;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GrnDraftFactory {

    private final GrnStatusRepository statusRepository;
    private final GrnPartRequestItemRepository grnPartRepository;

    public Grn createBalanceDraft(Grn originalGrn, BigDecimal totalBalanceQty) {
        // 1. Initialize the new Header with DRAFT status
        Grn nextDraft = Grn.builder()
                .branch(originalGrn.getBranch())
                .partrequest(originalGrn.getPartrequest())
                .number(generateNextNumber(originalGrn.getNumber()))
                .grnstatus(statusRepository.findByName("DRAFT").orElseThrow())
                .remarks("Balance draft for " + originalGrn.getNumber())
                .build();

        // 2. Clone only the items that still have a pending balance
        List<GrnPartRequestItem> balanceItems = originalGrn.getGrnpartrequestitems().stream()
                .map(item -> {
                    // DYNAMIC CALCULATION: Total ordered - Sum of all finalized receipts
                    BigDecimal remaining = calculateRemaining(item.getPartrequestitem());

                    if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                        return GrnPartRequestItem.builder()
                                .grn(nextDraft)
                                .partrequestitem(item.getPartrequestitem())
                                .quantity(remaining) // Set the balance as the new 'Expected' qty
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        nextDraft.setGrnpartrequestitems(balanceItems);
        return nextDraft;
    }

    private BigDecimal calculateRemaining(PartRequestItem poLine) {
        // Run the JPQL query to sum all 'RECEIVED' and 'PARTIALLY_RECEIVED' quantities
        BigDecimal totalReceived = grnPartRepository.sumQuantityByPartRequestItemId(
                poLine.getId(),
                List.of("Received", "Partially Received")
        );

        BigDecimal received = (totalReceived != null) ? totalReceived : BigDecimal.ZERO;

        // Final Balance = Ordered Quantity - Total Sum from DB
        return poLine.getQuantity().subtract(received);
    }

    private String generateNextNumber(String oldNumber) {
        // Logic to maintain sequence (e.g., GRN-001 -> GRN-001-BAL)
        return oldNumber + "-BAL";
    }
}
