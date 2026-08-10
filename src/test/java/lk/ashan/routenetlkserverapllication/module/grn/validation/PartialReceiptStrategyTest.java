package lk.ashan.routenetlkserverapllication.module.grn.validation;

import lk.ashan.routenetlkserverapllication.module.grn.event.GrnProcessedEvent;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnState;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnStatusFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartialReceiptStrategyTest {

    @Mock
    private GrnStatusFactory statusFactory;

    @Mock
    private GrnStatusRepository statusRepository;

    @Mock
    private GrnRepository grnRepository;

    @Mock
    private GrnDraftFactory grnDraftFactory;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PartialReceiptStrategy partialReceiptStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsApplicable() {
        BigDecimal receivedQty = BigDecimal.valueOf(50);
        BigDecimal expectedQty = BigDecimal.valueOf(100);

        assertTrue(partialReceiptStrategy.isApplicable(receivedQty, expectedQty));
    }

    @Test
    void testProcess() {
        // Arrange
        Grn currentGrn = Grn.builder()
                .id(1)
                .grnstatus(GrnStatus.builder().name("Draft").build())
                .build();

        GrnContext context = GrnContext.builder()
                .grn(currentGrn)
                .partRequestId(123)
                .receivedQty(BigDecimal.valueOf(50))
                .expectedQty(BigDecimal.valueOf(100))
                .build();

        GrnStatus partialStatus = GrnStatus.builder().name("Partially Received").build();
        GrnState currentState = mock(GrnState.class);

        Grn nextDraft = Grn.builder()
                .id(2)
                .build();

        when(statusRepository.findByName("Partially Received")).thenReturn(Optional.of(partialStatus));
        when(statusFactory.getState("Draft")).thenReturn(currentState);
        when(grnDraftFactory.createBalanceDraft(currentGrn, BigDecimal.valueOf(50))).thenReturn(nextDraft);

        // Act
        partialReceiptStrategy.process(context);

        // Assert
        verify(currentState).transitionTo(currentGrn, partialStatus);
        verify(grnRepository).saveAndFlush(currentGrn);
        verify(grnRepository).save(nextDraft);

        ArgumentCaptor<GrnProcessedEvent> eventCaptor = ArgumentCaptor.forClass(GrnProcessedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        GrnProcessedEvent publishedEvent = eventCaptor.getValue();
        assertEquals(123, publishedEvent.partRequestId());
        assertEquals(1, publishedEvent.grnId());
        assertEquals("Partially Received", publishedEvent.statusName());
    }
}
