package lk.ashan.routenetlkserverapllication.module.farecollection.validation;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import lk.ashan.routenetlkserverapllication.module.farecollection.repository.FareCollectionRepository;
import lk.ashan.routenetlkserverapllication.module.farecollection.service.TicketMachineService;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareCollectionValidatorTest {

    @Mock
    private FareCollectionRepository fareCollectionRepository;

    @Mock
    private TripExecutionRepository tripExecutionRepository;

    @Mock
    private TicketMachineService ticketMachineService;

    @InjectMocks
    private FareCollectionValidator validator;


    // =========================================================
    // validateCreate() - Financial Sanity
    // =========================================================

    @Test
    void validateCreate_shouldThrowExceptionWhenTotalTicketsAreNegative() {

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        -1,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );

        verifyNoInteractions(
                fareCollectionRepository,
                tripExecutionRepository,
                ticketMachineService
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenCashCollectedIsNegative() {

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.valueOf(-1),
                        BigDecimal.TEN
                )
        );

        verifyNoInteractions(
                fareCollectionRepository,
                tripExecutionRepository,
                ticketMachineService
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenDigitalPaymentsAreNegative() {

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(-1)
                )
        );

        verifyNoInteractions(
                fareCollectionRepository,
                tripExecutionRepository,
                ticketMachineService
        );
    }

    @Test
    void validateCreate_shouldPassFinancialSanityWhenValuesAreZero() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        TripExecution tripExecution =
                mock(TripExecution.class, RETURNS_DEEP_STUBS);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.of(tripExecution));

        when(tripExecution
                .getTripexecutionstatus()
                .getName())
                .thenReturn("COMPLETED");

        when(tripExecution
                .getTrip()
                .getBranch()
                .getId())
                .thenReturn(1);

        TicketMachine machine =
                mock(TicketMachine.class, RETURNS_DEEP_STUBS);

        when(ticketMachineService.getById(1))
                .thenReturn(machine);

        when(machine
                .getBranch()
                .getId())
                .thenReturn(1);

        assertDoesNotThrow(
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        0,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                )
        );
    }


    // =========================================================
    // validateCreate() - Duplicate Submission
    // =========================================================

    @Test
    void validateCreate_shouldThrowExceptionWhenDuplicateSubmissionExists() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );

        verify(fareCollectionRepository)
                .existsByTripexecution_Id(1);

        verifyNoInteractions(
                tripExecutionRepository,
                ticketMachineService
        );
    }


    // =========================================================
    // validateCreate() - Trip Execution
    // =========================================================

    @Test
    void validateCreate_shouldThrowExceptionWhenTripExecutionNotFound() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );

        verify(fareCollectionRepository)
                .existsByTripexecution_Id(1);

        verify(tripExecutionRepository)
                .findById(1);

        verifyNoInteractions(ticketMachineService);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenTripIsNotCompleted() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        TripExecution tripExecution =
                mock(TripExecution.class, RETURNS_DEEP_STUBS);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.of(tripExecution));

        when(tripExecution
                .getTripexecutionstatus()
                .getName())
                .thenReturn("IN_PROGRESS");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );

        verify(fareCollectionRepository)
                .existsByTripexecution_Id(1);

        verify(tripExecutionRepository)
                .findById(1);

        /*
         * Machine is loaded before validateTripState(),
         * so this interaction is expected.
         */
        verify(ticketMachineService)
                .getById(1);
    }

    @Test
    void validateCreate_shouldPassWhenTripIsCompleted() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        TripExecution tripExecution =
                mock(TripExecution.class, RETURNS_DEEP_STUBS);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.of(tripExecution));

        when(tripExecution
                .getTripexecutionstatus()
                .getName())
                .thenReturn("COMPLETED");

        when(tripExecution
                .getTrip()
                .getBranch()
                .getId())
                .thenReturn(1);

        TicketMachine machine =
                mock(TicketMachine.class, RETURNS_DEEP_STUBS);

        when(ticketMachineService.getById(1))
                .thenReturn(machine);

        when(machine
                .getBranch()
                .getId())
                .thenReturn(1);

        assertDoesNotThrow(
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );
    }


    // =========================================================
    // validateCreate() - Branch Boundaries
    // =========================================================

    @Test
    void validateCreate_shouldThrowExceptionWhenTicketMachineBelongsToDifferentBranch() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        TripExecution tripExecution =
                mock(TripExecution.class, RETURNS_DEEP_STUBS);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.of(tripExecution));

        when(tripExecution
                .getTripexecutionstatus()
                .getName())
                .thenReturn("COMPLETED");

        TicketMachine machine =
                mock(TicketMachine.class, RETURNS_DEEP_STUBS);

        when(ticketMachineService.getById(1))
                .thenReturn(machine);

        when(machine
                .getBranch()
                .getId())
                .thenReturn(2);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenTripExecutionBelongsToDifferentBranch() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        TripExecution tripExecution =
                mock(TripExecution.class, RETURNS_DEEP_STUBS);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.of(tripExecution));

        when(tripExecution
                .getTripexecutionstatus()
                .getName())
                .thenReturn("COMPLETED");

        when(tripExecution
                .getTrip()
                .getBranch()
                .getId())
                .thenReturn(2);

        TicketMachine machine =
                mock(TicketMachine.class, RETURNS_DEEP_STUBS);

        when(ticketMachineService.getById(1))
                .thenReturn(machine);

        when(machine
                .getBranch()
                .getId())
                .thenReturn(1);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );
    }


    // =========================================================
    // validateCreate() - Full Valid Flow
    // =========================================================

    @Test
    void validateCreate_shouldPassWhenAllValidationsPass() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        TripExecution tripExecution =
                mock(TripExecution.class, RETURNS_DEEP_STUBS);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.of(tripExecution));

        when(tripExecution
                .getTripexecutionstatus()
                .getName())
                .thenReturn("COMPLETED");

        when(tripExecution
                .getTrip()
                .getBranch()
                .getId())
                .thenReturn(1);

        TicketMachine machine =
                mock(TicketMachine.class, RETURNS_DEEP_STUBS);

        when(ticketMachineService.getById(1))
                .thenReturn(machine);

        when(machine
                .getBranch()
                .getId())
                .thenReturn(1);

        assertDoesNotThrow(
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        10,
                        BigDecimal.TEN,
                        BigDecimal.TEN
                )
        );

        verify(fareCollectionRepository)
                .existsByTripexecution_Id(1);

        verify(tripExecutionRepository)
                .findById(1);

        verify(ticketMachineService)
                .getById(1);
    }


    // =========================================================
    // validateCreate() - Null Financial Values
    // =========================================================

    @Test
    void validateCreate_shouldSkipFinancialValidationWhenFinancialValuesAreNull() {

        when(fareCollectionRepository.existsByTripexecution_Id(1))
                .thenReturn(false);

        TripExecution tripExecution =
                mock(TripExecution.class, RETURNS_DEEP_STUBS);

        when(tripExecutionRepository.findById(1))
                .thenReturn(Optional.of(tripExecution));

        when(tripExecution
                .getTripexecutionstatus()
                .getName())
                .thenReturn("COMPLETED");

        when(tripExecution
                .getTrip()
                .getBranch()
                .getId())
                .thenReturn(1);

        TicketMachine machine =
                mock(TicketMachine.class, RETURNS_DEEP_STUBS);

        when(ticketMachineService.getById(1))
                .thenReturn(machine);

        when(machine
                .getBranch()
                .getId())
                .thenReturn(1);

        assertDoesNotThrow(
                () -> validator.validateCreate(
                        1,
                        1,
                        1,
                        null,
                        null,
                        null
                )
        );
    }
}