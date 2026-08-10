package lk.ashan.routenetlkserverapllication.module.partreqest.validation;


import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class QuantityValidationTest {

    private QuantityValidation validation;

    @BeforeEach
    void setUp() {
        validation = new QuantityValidation();
    }

    @Test
    void validate_shouldThrowExceptionWhenQuantityIsZeroOrNegative() {
        // Arrange
        PartSummaryDto partSummary = PartSummaryDto.builder()
                .id(1)
                .name("Test Part")
                .build();

        PartRequestItemDto item = PartRequestItemDto.builder()
                .id(1)
                .quantity(BigDecimal.ZERO)
                .part(partSummary)
                .build();

        PartRequestValidationContext context = PartRequestValidationContext.builder()
                .partRequestId(1)
                .branchId(1)
                .items(List.of(item))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validation.validate(context));
    }

    @Test
    void validate_shouldNotThrowExceptionWhenQuantityIsPositive() {
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
                .items(List.of(item))
                .build();

        // Act & Assert
        validation.validate(context);
    }
}
