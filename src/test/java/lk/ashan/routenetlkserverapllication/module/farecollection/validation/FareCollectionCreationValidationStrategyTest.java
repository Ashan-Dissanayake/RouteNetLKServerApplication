package lk.ashan.routenetlkserverapllication.module.farecollection.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import lk.ashan.routenetlkserverapllication.module.farecollection.repository.FareCollectionRepository;
import lk.ashan.routenetlkserverapllication.module.farecollection.service.TicketMachineService;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FareCollectionCreationValidationStrategyTest {

    private FareCollectionRepository fareCollectionRepository;
    private TripExecutionRepository tripExecutionRepository;
    private TicketMachineService ticketMachineService;
    private FareCollectionCreationValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        fareCollectionRepository = Mockito.mock(FareCollectionRepository.class);
        tripExecutionRepository = Mockito.mock(TripExecutionRepository.class);
        ticketMachineService = Mockito.mock(TicketMachineService.class);
        validationStrategy = new FareCollectionCreationValidationStrategy(
                fareCollectionRepository,
                tripExecutionRepository,
                ticketMachineService
        );
    }

    @Test
    void validate_ShouldThrowException_WhenTotalTicketsAreNegative() {
        // Arrange
        FareCollectionValidationContext context = FareCollectionValidationContext.builder()
                .totalTickets(-1)
                .cashCollected(BigDecimal.TEN)
                .digitalPayments(BigDecimal.TEN)
                .tripExecutionId(1)
                .ticketMachineId(1)
                .branchId(1)
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenCashCollectedIsNegative() {
        // Arrange
        FareCollectionValidationContext context = FareCollectionValidationContext.builder()
                .totalTickets(10)
                .cashCollected(BigDecimal.valueOf(-1))
                .digitalPayments(BigDecimal.TEN)
                .tripExecutionId(1)
                .ticketMachineId(1)
                .branchId(1)
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenDuplicateSubmissionExists() {
        // Arrange
        FareCollectionValidationContext context = FareCollectionValidationContext.builder()
                .totalTickets(10)
                .cashCollected(BigDecimal.TEN)
                .digitalPayments(BigDecimal.TEN)
                .tripExecutionId(1)
                .ticketMachineId(1)
                .branchId(1)
                .build();

        when(fareCollectionRepository.existsByTripexecution_Id(1)).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenTripExecutionNotFound() {
        // Arrange
        FareCollectionValidationContext context = FareCollectionValidationContext.builder()
                .totalTickets(10)
                .cashCollected(BigDecimal.TEN)
                .digitalPayments(BigDecimal.TEN)
                .tripExecutionId(1)
                .ticketMachineId(1)
                .branchId(1)
                .build();

        when(tripExecutionRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenTripStateIsNotCompleted() {
        // Arrange
        FareCollectionValidationContext context = FareCollectionValidationContext.builder()
                .totalTickets(10)
                .cashCollected(BigDecimal.TEN)
                .digitalPayments(BigDecimal.TEN)
                .tripExecutionId(1)
                .ticketMachineId(1)
                .branchId(1)
                .build();

        TripExecution tripExecution = mock(TripExecution.class);
        TripExecutionStatus status = mock(TripExecutionStatus.class);
        when(status.getName()).thenReturn("IN_PROGRESS");
        when(tripExecution.getTripexecutionstatus()).thenReturn(status);
        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenTicketMachineBranchDoesNotMatch() {

        // Arrange
        FareCollectionValidationContext context =
                FareCollectionValidationContext.builder()
                        .totalTickets(10)
                        .cashCollected(BigDecimal.TEN)
                        .digitalPayments(BigDecimal.TEN)
                        .tripExecutionId(1)
                        .ticketMachineId(1)
                        .branchId(1)
                        .build();

        TripExecution tripExecution = mock(TripExecution.class);
        TripExecutionStatus tripExecutionStatus = mock(TripExecutionStatus.class);

        TicketMachine ticketMachine = mock(TicketMachine.class);
        Branch ticketMachineBranch = mock(Branch.class);

        // Trip Execution
        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));
        when(tripExecution.getTripexecutionstatus()).thenReturn(tripExecutionStatus);
        when(tripExecutionStatus.getName()).thenReturn("COMPLETED");

        // Ticket Machine
        when(ticketMachineService.getById(1)).thenReturn(ticketMachine);
        when(ticketMachine.getBranch()).thenReturn(ticketMachineBranch);
        when(ticketMachineBranch.getId()).thenReturn(2);

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> validationStrategy.validate(context)
        );
    }

    @Test
    void validate_ShouldPass_WhenAllValidationsPass() {
        // Arrange
        FareCollectionValidationContext context =
                FareCollectionValidationContext.builder()
                        .totalTickets(10)
                        .cashCollected(BigDecimal.TEN)
                        .digitalPayments(BigDecimal.TEN)
                        .tripExecutionId(1)
                        .ticketMachineId(1)
                        .branchId(1)
                        .build();

        TripExecution tripExecution = mock(TripExecution.class);
        TripExecutionStatus status = mock(TripExecutionStatus.class);

        Trip trip = mock(Trip.class);
        Branch tripBranch = mock(Branch.class);

        TicketMachine ticketMachine = mock(TicketMachine.class);
        Branch ticketMachineBranch = mock(Branch.class);

        // Trip Execution status
        when(tripExecution.getTripexecutionstatus()).thenReturn(status);
        when(status.getName()).thenReturn("COMPLETED");

        // Trip -> Branch
        when(tripExecution.getTrip()).thenReturn(trip);
        when(trip.getBranch()).thenReturn(tripBranch);
        when(tripBranch.getId()).thenReturn(1);

        // Trip Execution repository
        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));

        // Ticket Machine -> Branch
        when(ticketMachineService.getById(1)).thenReturn(ticketMachine);
        when(ticketMachine.getBranch()).thenReturn(ticketMachineBranch);
        when(ticketMachineBranch.getId()).thenReturn(1);

        // Act & Assert
        assertDoesNotThrow(() -> validationStrategy.validate(context));
    }
}
