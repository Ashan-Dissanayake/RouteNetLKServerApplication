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

@Component
@RequiredArgsConstructor
public class PartialReceiptStrategy implements GrnProcessingStrategy {

    private final GrnStatusFactory statusFactory;
    private final GrnStatusRepository statusRepository;
    private final GrnRepository grnRepository;
    private final GrnDraftFactory grnDraftFactory;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public boolean isApplicable(BigDecimal receivedQty, BigDecimal expectedQty) {
        return receivedQty.compareTo(expectedQty) < 0;
    }

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

        // Notify PartRequest Module: "I received some items, but not all"
        eventPublisher.publishEvent(new GrnProcessedEvent(
                context.getPartRequestId(),
                currentGrn.getId(),
                "Partially Received"
        ));
    }

}
