package lk.ashan.routenetlkserverapllication.module.grn.validation;


import lk.ashan.routenetlkserverapllication.module.grn.event.GrnProcessedEvent;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
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

class FullReceiptStrategyTest {

    @Mock
    private GrnStatusFactory statusFactory;

    @Mock
    private GrnStatusRepository statusRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private FullReceiptStrategy fullReceiptStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsApplicable() {
        BigDecimal receivedQty = BigDecimal.valueOf(100);
        BigDecimal expectedQty = BigDecimal.valueOf(100);

        assertTrue(fullReceiptStrategy.isApplicable(receivedQty, expectedQty));
    }

    @Test
    void testProcess() {
        // Arrange
        Grn grn = Grn.builder()
                .id(1)
                .grnstatus(GrnStatus.builder().name("Draft").build())
                .build();

        GrnContext context = GrnContext.builder()
                .grn(grn)
                .partRequestId(123)
                .receivedQty(BigDecimal.valueOf(100))
                .expectedQty(BigDecimal.valueOf(100))
                .build();

        GrnStatus receivedStatus = GrnStatus.builder().name("Received").build();
        GrnState currentState = mock(GrnState.class);

        when(statusRepository.findByName("Received")).thenReturn(Optional.of(receivedStatus));
        when(statusFactory.getState("Draft")).thenReturn(currentState);

        // Act
        fullReceiptStrategy.process(context);

        // Assert
        verify(currentState).transitionTo(grn, receivedStatus);
        ArgumentCaptor<GrnProcessedEvent> eventCaptor = ArgumentCaptor.forClass(GrnProcessedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        GrnProcessedEvent publishedEvent = eventCaptor.getValue();
        assertEquals(123, publishedEvent.partRequestId());
        assertEquals(1, publishedEvent.grnId());
        assertEquals("Received", publishedEvent.statusName());
    }
}
