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

/**
 * Service class responsible for creating draft GRNs (Goods Receipt Notes)
 * based on the remaining balance quantities.
 */
@Service
@RequiredArgsConstructor
public class GrnDraftFactory {

    private final GrnStatusRepository statusRepository;
    private final GrnPartRequestItemRepository grnPartRepository;

    /**
     * Creates a new draft GRN based on the remaining balance quantities of the original GRN.
     *
     * @param originalGrn The original GRN from which the draft is created.
     * @param totalBalanceQty The total balance quantity to be considered for the draft.
     * @return A new GRN object with DRAFT status and balance items.
     * @throws IllegalArgumentException if the DRAFT status is not found in the repository.
     */
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

    /**
     * Calculates the remaining quantity for a given PartRequestItem.
     *
     * @param poLine The PartRequestItem for which the remaining quantity is calculated.
     * @return The remaining quantity as a BigDecimal.
     */
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

    /**
     * Generates the next GRN number in sequence by appending a suffix.
     *
     * @param oldNumber The original GRN number.
     * @return The new GRN number with a "-BAL" suffix.
     */
    private String generateNextNumber(String oldNumber) {
        // Logic to maintain sequence (e.g., GRN-001 -> GRN-001-BAL)
        return oldNumber + "-BAL";
    }
}
