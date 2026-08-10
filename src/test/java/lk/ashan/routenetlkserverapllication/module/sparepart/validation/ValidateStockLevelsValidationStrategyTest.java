package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateStockLevelsValidationStrategyTest {

    private validateStockLevelsValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        validationStrategy = new validateStockLevelsValidationStrategy();
    }

    @Test
    void validate_ShouldThrowException_WhenMaxLevelLessThanOrEqualToRop() {
        PartContext context = PartContext.builder()
                .maxlevel(BigDecimal.valueOf(10))
                .rop(BigDecimal.valueOf(15))
                .qoh(BigDecimal.valueOf(5))
                .build();

        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenQohExceedsMaxLevel() {
        PartContext context = PartContext.builder()
                .maxlevel(BigDecimal.valueOf(10))
                .rop(BigDecimal.valueOf(5))
                .qoh(BigDecimal.valueOf(15))
                .build();

        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenExistingQohExceedsMaxLevel() {
        PartContext context = PartContext.builder()
                .maxlevel(BigDecimal.valueOf(10))
                .rop(BigDecimal.valueOf(5))
                .qoh(BigDecimal.valueOf(5))
                .existingQoh(BigDecimal.valueOf(15))
                .build();

        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenValidContext() {
        PartContext context = PartContext.builder()
                .maxlevel(BigDecimal.valueOf(20))
                .rop(BigDecimal.valueOf(10))
                .qoh(BigDecimal.valueOf(15))
                .build();

        assertDoesNotThrow(() -> validationStrategy.validate(context));
    }
}
