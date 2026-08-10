package lk.ashan.routenetlkserverapllication.module.partreqest.validation;


import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DuplicatePartInOpenRequestsValidationTest {

    private PartRequestRepository repository;
    private DuplicatePartInOpenRequestsValidation validation;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PartRequestRepository.class);
        validation = new DuplicatePartInOpenRequestsValidation(repository);
    }

    @Test
    void validate_shouldThrowExceptionWhenDuplicatePartExists() {
        // Arrange
        PartSummaryDto partSummary = PartSummaryDto.builder()
                .id(1)
                .name("Test Part")
                .build();

        PartRequestItemDto item = PartRequestItemDto.builder()
                .id(1)
                .quantity(BigDecimal.ONE)
                .part(partSummary)
                .build();

        PartRequestValidationContext context = PartRequestValidationContext.builder()
                .partRequestId(1)
                .branchId(1)
                .requestedate(LocalDate.now())
                .items(List.of(item))
                .build();

        when(repository.existsByBranchAndPartAndStatusInAndDoRequested(
                context.getBranchId(),
                item.getPart().getId(),
                List.of("Pending", "Approved"),
                context.getRequestedate()
        )).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validation.validate(context));

        verify(repository, times(1)).existsByBranchAndPartAndStatusInAndDoRequested(
                context.getBranchId(),
                item.getPart().getId(),
                List.of("Pending", "Approved"),
                context.getRequestedate()
        );
    }

    @Test
    void validate_shouldNotThrowExceptionWhenNoDuplicatePartExists() {
        // Arrange
        PartSummaryDto partSummary = PartSummaryDto.builder()
                .id(1)
                .name("Test Part")
                .build();

        PartRequestItemDto item = PartRequestItemDto.builder()
                .id(1)
                .quantity(BigDecimal.ONE)
                .part(partSummary)
                .build();

        PartRequestValidationContext context = PartRequestValidationContext.builder()
                .partRequestId(1)
                .branchId(1)
                .requestedate(LocalDate.now())
                .items(List.of(item))
                .build();

        when(repository.existsByBranchAndPartAndStatusInAndDoRequested(
                context.getBranchId(),
                item.getPart().getId(),
                List.of("Pending", "Approved"),
                context.getRequestedate()
        )).thenReturn(false);

        // Act
        validation.validate(context);

        // Assert
        verify(repository, times(1)).existsByBranchAndPartAndStatusInAndDoRequested(
                context.getBranchId(),
                item.getPart().getId(),
                List.of("Pending", "Approved"),
                context.getRequestedate()
        );
    }
}
