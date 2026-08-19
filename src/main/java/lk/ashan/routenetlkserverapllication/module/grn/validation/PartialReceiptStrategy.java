package lk.ashan.routenetlkserverapllication.module.grn.validation;

import lk.ashan.routenetlkserverapllication.module.grn.event.GrnProcessedEvent;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnState;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnStatusFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Strategy implementation for processing GRNs (Goods Receipt Notes) with partial receipt status.
 * This class handles the transition of GRN states and the creation of balance drafts for remaining quantities.
 */
@Component
@RequiredArgsConstructor
public class PartialReceiptStrategy implements GrnProcessingStrategy {

    private final GrnStatusFactory statusFactory;
    private final GrnStatusRepository statusRepository;
    private final GrnRepository grnRepository;
    private final GrnDraftFactory grnDraftFactory;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Determines if this strategy is applicable based on the received and expected quantities.
     *
     * @param receivedQty the quantity of goods received
     * @param expectedQty the quantity of goods expected
     * @return true if the received quantity is less than the expected quantity, false otherwise
     */
    @Override
    public boolean isApplicable(BigDecimal receivedQty, BigDecimal expectedQty) {
        return receivedQty.compareTo(expectedQty) < 0;
    }

    /**
     * Processes the GRN context by transitioning the GRN state to "Partially Received",
     * saving the current GRN, creating a balance draft for the remaining quantity, and
     * publishing a GRN processed event.
     *
     * @param context the GRN context containing the current GRN and related data
     * @throws IllegalStateException if the "Partially Received" status is not found in the repository
     */
    @Override
    public void process(GrnContext context) {
        Grn currentGrn = context.getGrn();
        GrnStatus partialStatus = statusRepository.findByName("Partially Received").orElseThrow();

        // State Transition (DRAFT -> PARTIALLY_RECEIVED)
        GrnState currentState = statusFactory.getState(currentGrn.getGrnstatus().getName());
        currentState.transitionTo(currentGrn, partialStatus);

        grnRepository.saveAndFlush(currentGrn);

        // Create the next balance Draft
        BigDecimal balanceQty = context.getExpectedQty().subtract(context.getReceivedQty());
        Grn nextDraft = grnDraftFactory.createBalanceDraft(currentGrn, balanceQty);
        grnRepository.save(nextDraft);

        eventPublisher.publishEvent(new GrnProcessedEvent(
                context.getPartRequestId(),
                currentGrn.getId(),
                "Partially Received"
        ));
    }

}
