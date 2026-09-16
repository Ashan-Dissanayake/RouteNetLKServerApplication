package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PartValidatorTest {

    private PartValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PartValidator();
    }

    // =========================================================
    // CREATE - STOCK LEVEL VALIDATION
    // =========================================================

    @Test
    void validateCreate_shouldThrowExceptionWhenMaxLevelIsLessThanOrEqualToRop() {

        PartCreateRequestDto request =
                mock(PartCreateRequestDto.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(15));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(5));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenQohExceedsMaxLevel() {

        PartCreateRequestDto request =
                mock(PartCreateRequestDto.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(5));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(15));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldPassWhenStockLevelsAreValid() {

        PartCreateRequestDto request =
                mock(PartCreateRequestDto.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(20));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(15));

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldPassWhenQohEqualsMaxLevel() {

        PartCreateRequestDto request =
                mock(PartCreateRequestDto.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(20));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(20));

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldPassWhenMaxLevelIsNull() {

        PartCreateRequestDto request =
                mock(PartCreateRequestDto.class);

        when(request.getMaxlevel())
                .thenReturn(null);

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(5));

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldPassWhenRopIsNull() {

        PartCreateRequestDto request =
                mock(PartCreateRequestDto.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(20));

        when(request.getRop())
                .thenReturn(null);

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(5));

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldPassWhenQohIsNull() {

        PartCreateRequestDto request =
                mock(PartCreateRequestDto.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(20));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getQoh())
                .thenReturn(null);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    // =========================================================
    // UPDATE - STOCK LEVEL VALIDATION
    // =========================================================

    @Test
    void validateUpdate_shouldThrowExceptionWhenMaxLevelIsLessThanOrEqualToRop() {

        PartUpdateRequestDto request =
                mock(PartUpdateRequestDto.class);

        Part existingPart = mock(Part.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(15));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(5));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request, existingPart)
        );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenQohExceedsMaxLevel() {

        PartUpdateRequestDto request =
                mock(PartUpdateRequestDto.class);

        Part existingPart = mock(Part.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(5));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(15));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request, existingPart)
        );
    }

    @Test
    void validateUpdate_shouldPassWhenStockLevelsAreValid() {

        PartUpdateRequestDto request =
                mock(PartUpdateRequestDto.class);

        Part existingPart = mock(Part.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(20));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(15));

        when(existingPart.getQoh())
                .thenReturn(BigDecimal.valueOf(12));

        assertDoesNotThrow(
                () -> validator.validateUpdate(request, existingPart)
        );
    }

    // =========================================================
    // UPDATE - CURRENT STOCK VS NEW MAX LEVEL
    // =========================================================

    @Test
    void validateUpdate_shouldThrowExceptionWhenExistingQohExceedsNewMaxLevel() {

        PartUpdateRequestDto request =
                mock(PartUpdateRequestDto.class);

        Part existingPart = mock(Part.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(5));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(5));

        when(existingPart.getQoh())
                .thenReturn(BigDecimal.valueOf(15));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request, existingPart)
        );
    }

    @Test
    void validateUpdate_shouldPassWhenExistingQohEqualsNewMaxLevel() {

        PartUpdateRequestDto request =
                mock(PartUpdateRequestDto.class);

        Part existingPart = mock(Part.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(5));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(5));

        when(existingPart.getQoh())
                .thenReturn(BigDecimal.valueOf(10));

        assertDoesNotThrow(
                () -> validator.validateUpdate(request, existingPart)
        );
    }

    @Test
    void validateUpdate_shouldPassWhenNewMaxLevelIsNull() {

        PartUpdateRequestDto request =
                mock(PartUpdateRequestDto.class);

        Part existingPart = mock(Part.class);

        when(request.getMaxlevel())
                .thenReturn(null);

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(5));

        when(existingPart.getQoh())
                .thenReturn(BigDecimal.valueOf(15));

        assertDoesNotThrow(
                () -> validator.validateUpdate(request, existingPart)
        );
    }

    @Test
    void validateUpdate_shouldPassWhenExistingQohIsNull() {

        PartUpdateRequestDto request =
                mock(PartUpdateRequestDto.class);

        Part existingPart = mock(Part.class);

        when(request.getMaxlevel())
                .thenReturn(BigDecimal.valueOf(20));

        when(request.getRop())
                .thenReturn(BigDecimal.valueOf(10));

        when(request.getQoh())
                .thenReturn(BigDecimal.valueOf(15));

        when(existingPart.getQoh())
                .thenReturn(null);

        assertDoesNotThrow(
                () -> validator.validateUpdate(request, existingPart)
        );
    }

    // =========================================================
    // DEACTIVATION VALIDATION
    // =========================================================

    @Test
    void validateDeactivation_shouldPassWhenPartsListIsEmpty() {

        assertDoesNotThrow(
                () -> validator.validateDeactivation(List.of())
        );
    }

    @Test
    void validateDeactivation_shouldPassWhenPartIsActive() {

        Part part = mock(Part.class, RETURNS_DEEP_STUBS);

        when(part.getPartstatus().getName())
                .thenReturn("ACTIVE");

        assertDoesNotThrow(
                () -> validator.validateDeactivation(List.of(part))
        );
    }

    @Test
    void validateDeactivation_shouldThrowExceptionWhenPartIsDecommissioned() {

        Part part = mock(Part.class, RETURNS_DEEP_STUBS);

        when(part.getPartstatus().getName())
                .thenReturn("DECOMMISSIONED");

        when(part.getId())
                .thenReturn(1);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateDeactivation(List.of(part))
        );
    }

    @Test
    void validateDeactivation_shouldThrowExceptionWhenPartStatusIsCaseInsensitiveDecommissioned() {

        Part part = mock(Part.class, RETURNS_DEEP_STUBS);

        when(part.getPartstatus().getName())
                .thenReturn("decommissioned");

        when(part.getId())
                .thenReturn(1);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateDeactivation(List.of(part))
        );
    }

    @Test
    void validateDeactivation_shouldPassWhenPartStatusIsNull() {

        Part part = mock(Part.class);

        when(part.getPartstatus())
                .thenReturn(null);

        assertDoesNotThrow(
                () -> validator.validateDeactivation(List.of(part))
        );
    }

    @Test
    void validateDeactivation_shouldThrowExceptionWhenAnyPartIsDecommissioned() {

        Part activePart = mock(Part.class, RETURNS_DEEP_STUBS);

        when(activePart.getPartstatus().getName())
                .thenReturn("ACTIVE");

        Part decommissionedPart =
                mock(Part.class, RETURNS_DEEP_STUBS);

        when(decommissionedPart.getPartstatus().getName())
                .thenReturn("DECOMMISSIONED");

        when(decommissionedPart.getId())
                .thenReturn(2);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateDeactivation(
                        List.of(
                                activePart,
                                decommissionedPart
                        )
                )
        );
    }

    // =========================================================
    // TEST FIXTURES
    // =========================================================

    private Part createPartWithStatus(
            Integer id,
            String statusName
    ) {

        Part part = mock(Part.class, RETURNS_DEEP_STUBS);

        when(part.getId())
                .thenReturn(id);

        when(part.getPartstatus().getName())
                .thenReturn(statusName);

        return part;
    }
}