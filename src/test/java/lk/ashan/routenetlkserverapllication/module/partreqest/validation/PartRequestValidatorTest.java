package lk.ashan.routenetlkserverapllication.module.partreqest.validation;


import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartRequestValidatorTest {

    @Mock
    private PartRequestRepository partRequestRepository;

    @InjectMocks
    private PartRequestValidator validator;


    // =========================
    // CREATE - ITEMS
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenItemsAreNull() {

        PartRequestCreateRequestDto request =
                PartRequestCreateRequestDto.builder()
                        .partrequestitems(null)
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(partRequestRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenItemsAreEmpty() {

        PartRequestCreateRequestDto request =
                PartRequestCreateRequestDto.builder()
                        .partrequestitems(List.of())
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(partRequestRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenQuantityIsNull() {

        PartSummaryDto part = createPart(1, "Engine Oil");

        PartRequestItemDto item =
                PartRequestItemDto.builder()
                        .part(part)
                        .quantity(null)
                        .build();

        PartRequestCreateRequestDto request =
                createRequest(List.of(item));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(partRequestRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenQuantityIsZero() {

        PartSummaryDto part = createPart(1, "Engine Oil");

        PartRequestItemDto item =
                PartRequestItemDto.builder()
                        .part(part)
                        .quantity(BigDecimal.ZERO)
                        .build();

        PartRequestCreateRequestDto request =
                createRequest(List.of(item));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(partRequestRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenQuantityIsNegative() {

        PartSummaryDto part = createPart(1, "Engine Oil");

        PartRequestItemDto item =
                PartRequestItemDto.builder()
                        .part(part)
                        .quantity(new BigDecimal("-1"))
                        .build();

        PartRequestCreateRequestDto request =
                createRequest(List.of(item));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(partRequestRepository);
    }

    @Test
    void validateCreate_shouldAllowPositiveQuantity() {

        PartSummaryDto part = createPart(1, "Engine Oil");

        PartRequestItemDto item =
                PartRequestItemDto.builder()
                        .part(part)
                        .quantity(BigDecimal.ONE)
                        .build();

        PartRequestCreateRequestDto request =
                createRequest(List.of(item));

        mockNoDuplicateOpenRequest(1, 1);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldAllowDecimalQuantityGreaterThanZero() {

        PartSummaryDto part = createPart(1, "Engine Oil");

        PartRequestItemDto item =
                PartRequestItemDto.builder()
                        .part(part)
                        .quantity(new BigDecimal("0.01"))
                        .build();

        PartRequestCreateRequestDto request =
                createRequest(List.of(item));

        mockNoDuplicateOpenRequest(1, 1);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }


    // =========================
    // CREATE - DUPLICATE ITEMS
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenSamePartAppearsTwice() {

        PartSummaryDto part = createPart(1, "Engine Oil");

        PartRequestItemDto firstItem =
                createItem(part, BigDecimal.ONE);

        PartRequestItemDto secondItem =
                createItem(part, BigDecimal.ONE.add(BigDecimal.ONE));

        PartRequestCreateRequestDto request =
                createRequest(
                        List.of(firstItem, secondItem)
                );

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(partRequestRepository);
    }

    @Test
    void validateCreate_shouldAllowDifferentParts() {

        PartSummaryDto firstPart =
                createPart(1, "Engine Oil");

        PartSummaryDto secondPart =
                createPart(2, "Brake Pad");

        PartRequestItemDto firstItem =
                createItem(firstPart, BigDecimal.ONE);

        PartRequestItemDto secondItem =
                createItem(secondPart, BigDecimal.ONE.add(BigDecimal.ONE));

        PartRequestCreateRequestDto request =
                createRequest(
                        List.of(firstItem, secondItem)
                );

        when(
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                1,
                                1,
                                List.of("Pending", "Approved"),
                                request.getDorequested()
                        )
        ).thenReturn(false);

        when(
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                1,
                                2,
                                List.of("Pending", "Approved"),
                                request.getDorequested()
                        )
        ).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldDetectDuplicatePartUsingPartId() {

        PartSummaryDto firstPart =
                createPart(1, "Engine Oil");

        PartSummaryDto secondPart =
                createPart(1, "Engine Oil");

        PartRequestItemDto firstItem =
                createItem(firstPart, BigDecimal.ONE);

        PartRequestItemDto secondItem =
                createItem(secondPart, BigDecimal.ONE);

        PartRequestCreateRequestDto request =
                createRequest(
                        List.of(firstItem, secondItem)
                );

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(partRequestRepository);
    }


    // =========================
    // CREATE - DUPLICATE OPEN REQUEST
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenOpenRequestAlreadyExists() {

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ONE);

        PartRequestCreateRequestDto request =
                createRequest(List.of(item));

        when(
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                1,
                                1,
                                List.of("Pending", "Approved"),
                                request.getDorequested()
                        )
        ).thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verify(
                partRequestRepository
        ).existsByBranchAndPartAndStatusInAndDoRequested(
                1,
                1,
                List.of("Pending", "Approved"),
                request.getDorequested()
        );
    }

    @Test
    void validateCreate_shouldAllowWhenNoOpenRequestExists() {

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ONE);

        PartRequestCreateRequestDto request =
                createRequest(List.of(item));

        mockNoDuplicateOpenRequest(1, 1);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );

        verify(
                partRequestRepository
        ).existsByBranchAndPartAndStatusInAndDoRequested(
                1,
                1,
                List.of("Pending", "Approved"),
                request.getDorequested()
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenSecondItemHasOpenRequest() {

        PartSummaryDto firstPart =
                createPart(1, "Engine Oil");

        PartSummaryDto secondPart =
                createPart(2, "Brake Pad");

        PartRequestItemDto firstItem =
                createItem(firstPart, BigDecimal.ONE);

        PartRequestItemDto secondItem =
                createItem(secondPart, BigDecimal.ONE);

        PartRequestCreateRequestDto request =
                createRequest(
                        List.of(firstItem, secondItem)
                );

        when(
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                1,
                                1,
                                List.of("Pending", "Approved"),
                                request.getDorequested()
                        )
        ).thenReturn(false);

        when(
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                1,
                                2,
                                List.of("Pending", "Approved"),
                                request.getDorequested()
                        )
        ).thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verify(
                partRequestRepository
        ).existsByBranchAndPartAndStatusInAndDoRequested(
                1,
                1,
                List.of("Pending", "Approved"),
                request.getDorequested()
        );

        verify(
                partRequestRepository
        ).existsByBranchAndPartAndStatusInAndDoRequested(
                1,
                2,
                List.of("Pending", "Approved"),
                request.getDorequested()
        );
    }


    // =========================
    // UPDATE
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenCurrentStatusIsApproved() {

        PartRequest request =
                createPartRequest("Approved");

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ONE);

        PartRequestUpdateRequestDto updateRequest =
                PartRequestUpdateRequestDto.builder()
                        .partrequestitems(List.of(item))
                        .build();

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(
                        request,
                        updateRequest
                )
        );

        verifyNoInteractions(partRequestRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenCurrentStatusIsRejected() {

        PartRequest request =
                createPartRequest("Rejected");

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ONE);

        PartRequestUpdateRequestDto updateRequest =
                PartRequestUpdateRequestDto.builder()
                        .partrequestitems(List.of(item))
                        .build();

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(
                        request,
                        updateRequest
                )
        );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenCurrentStatusIsCompleted() {

        PartRequest request =
                createPartRequest("Completed");

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ONE);

        PartRequestUpdateRequestDto updateRequest =
                PartRequestUpdateRequestDto.builder()
                        .partrequestitems(List.of(item))
                        .build();

        assertThrows(
                InvalidStateTransitionException.class,
                () -> validator.validateUpdate(
                        request,
                        updateRequest
                )
        );
    }

    @Test
    void validateUpdate_shouldAllowPendingRequestWithValidItems() {

        PartRequest request =
                createPartRequest("Pending");

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ONE);

        PartRequestUpdateRequestDto updateRequest =
                PartRequestUpdateRequestDto.builder()
                        .partrequestitems(List.of(item))
                        .build();

        assertDoesNotThrow(
                () -> validator.validateUpdate(
                        request,
                        updateRequest
                )
        );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenPendingRequestHasNoItems() {

        PartRequest request =
                createPartRequest("Pending");

        PartRequestUpdateRequestDto updateRequest =
                PartRequestUpdateRequestDto.builder()
                        .partrequestitems(List.of())
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(
                        request,
                        updateRequest
                )
        );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenPendingRequestHasZeroQuantity() {

        PartRequest request =
                createPartRequest("Pending");

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ZERO);

        PartRequestUpdateRequestDto updateRequest =
                PartRequestUpdateRequestDto.builder()
                        .partrequestitems(List.of(item))
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(
                        request,
                        updateRequest
                )
        );
    }

    @Test
    void validateUpdate_shouldAllowPendingStatusIgnoringCase() {

        PartRequest request =
                createPartRequest("pending");

        PartSummaryDto part =
                createPart(1, "Engine Oil");

        PartRequestItemDto item =
                createItem(part, BigDecimal.ONE);

        PartRequestUpdateRequestDto updateRequest =
                PartRequestUpdateRequestDto.builder()
                        .partrequestitems(List.of(item))
                        .build();

        assertDoesNotThrow(
                () -> validator.validateUpdate(
                        request,
                        updateRequest
                )
        );
    }


    // =========================
    // STATUS TRANSITIONS
    // =========================

    @Test
    void validateStatusTransition_shouldAllowSameStatus() {

        PartRequestStatus current =
                createStatus("PENDING");

        PartRequestStatus target =
                createStatus("PENDING");

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldAllowPendingToApproved() {

        PartRequestStatus current =
                createStatus("PENDING");

        PartRequestStatus target =
                createStatus("APPROVED");

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldAllowPendingToRejected() {

        PartRequestStatus current =
                createStatus("PENDING");

        PartRequestStatus target =
                createStatus("REJECTED");

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldAllowApprovedToCompleted() {

        PartRequestStatus current =
                createStatus("APPROVED");

        PartRequestStatus target =
                createStatus("COMPLETED");

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenPendingToCompleted() {

        PartRequestStatus current =
                createStatus("PENDING");

        PartRequestStatus target =
                createStatus("COMPLETED");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenPendingToReleased() {

        PartRequestStatus current =
                createStatus("PENDING");

        PartRequestStatus target =
                createStatus("RELEASED");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenApprovedToRejected() {

        PartRequestStatus current =
                createStatus("APPROVED");

        PartRequestStatus target =
                createStatus("REJECTED");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenRejectedTransitionsToPending() {

        PartRequestStatus current =
                createStatus("REJECTED");

        PartRequestStatus target =
                createStatus("PENDING");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenCompletedTransitionsToPending() {

        PartRequestStatus current =
                createStatus("COMPLETED");

        PartRequestStatus target =
                createStatus("PENDING");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenCurrentStatusIsUnknown() {

        PartRequestStatus current =
                createStatus("UNKNOWN");

        PartRequestStatus target =
                createStatus("APPROVED");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldIgnoreCase() {

        PartRequestStatus current =
                createStatus("pending");

        PartRequestStatus target =
                createStatus("approved");

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_shouldIgnoreLeadingAndTrailingSpaces() {

        PartRequestStatus current =
                createStatus("  PENDING  ");

        PartRequestStatus target =
                createStatus("  APPROVED  ");

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(
                        current,
                        target
                )
        );
    }


    // =========================
    // INITIAL STATUS
    // =========================

    @Test
    void validateInitialStatus_shouldAllowPendingStatus() {

        PartRequestStatus status =
                createStatus("PENDING");

        assertDoesNotThrow(
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldIgnoreCase() {

        PartRequestStatus status =
                createStatus("pending");

        assertDoesNotThrow(
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenStatusIsApproved() {

        PartRequestStatus status =
                createStatus("APPROVED");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenStatusIsRejected() {

        PartRequestStatus status =
                createStatus("REJECTED");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenStatusIsCompleted() {

        PartRequestStatus status =
                createStatus("COMPLETED");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateInitialStatus(status)
        );
    }


    // =========================
    // TEST HELPERS
    // =========================

    private PartSummaryDto createPart(
            Integer id,
            String name
    ) {
        PartSummaryDto part = new PartSummaryDto();
        part.setId(id);
        part.setName(name);
        return part;
    }

    private PartRequestItemDto createItem(
            PartSummaryDto part,
            BigDecimal quantity
    ) {
        return PartRequestItemDto.builder()
                .part(part)
                .quantity(quantity)
                .build();
    }

    private PartRequestCreateRequestDto createRequest(
            List<PartRequestItemDto> items
    ) {

        BranchSummaryDto branch = new BranchSummaryDto();
        branch.setId(1);

        return PartRequestCreateRequestDto.builder()
                .branch(branch)
                .dorequested(LocalDate.now())
                .partrequestitems(items)
                .build();
    }

    private PartRequest createPartRequest(
            String statusName
    ) {

        PartRequestStatus status =
                createStatus(statusName);

        PartRequest request =
                new PartRequest();

        request.setPartrequeststatus(status);

        return request;
    }

    private PartRequestStatus createStatus(
            String statusName
    ) {

        PartRequestStatus status =
                new PartRequestStatus();

        status.setName(statusName);

        return status;
    }

    private void mockNoDuplicateOpenRequest(Integer branchId, Integer partId) {

        when(
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                branchId,
                                partId,
                                List.of("Pending", "Approved"),
                                LocalDate.now()
                        )
        ).thenReturn(false);
    }
}
