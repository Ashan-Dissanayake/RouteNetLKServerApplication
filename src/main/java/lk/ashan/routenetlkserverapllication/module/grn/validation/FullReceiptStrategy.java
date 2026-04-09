package lk.ashan.routenetlkserverapllication.module.grn.validation;

import lk.ashan.routenetlkserverapllication.module.grn.event.GrnProcessedEvent;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnState;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnStatusFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class FullReceiptStrategy implements GrnProcessingStrategy {

    private final GrnStatusFactory statusFactory;
    private final GrnStatusRepository statusRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean isApplicable(BigDecimal receivedQty, BigDecimal expectedQty) {
        return receivedQty.compareTo(expectedQty) == 0;
    }

    @Override
    public void process(GrnContext context) {
        Grn grn = context.getGrn();
        GrnStatus receivedStatus = statusRepository.findByName("Received").orElseThrow();

        // State Transition (DRAFT -> RECEIVED)
        GrnState currentState = statusFactory.getState(grn.getGrnstatus().getName());
        currentState.transitionTo(grn, receivedStatus);

        // Notify PartRequest Module: "I am finished with this specific receipt"
        eventPublisher.publishEvent(new GrnProcessedEvent(
                context.getPartRequestId(),
                grn.getId(),
                "Received"
        ));
    }

}
