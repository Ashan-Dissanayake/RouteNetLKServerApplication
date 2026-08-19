package lk.ashan.routenetlkserverapllication.module.farecollection.validation;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import lk.ashan.routenetlkserverapllication.module.farecollection.repository.FareCollectionRepository;
import lk.ashan.routenetlkserverapllication.module.farecollection.service.TicketMachineService;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Validation strategy for fare collection creation.
 * Ensures that all business rules are adhered to during fare collection.
 */
@Component
@RequiredArgsConstructor
public class FareCollectionCreationValidationStrategy {

    private final FareCollectionRepository fareCollectionRepository;
    private final TripExecutionRepository tripExecutionRepository;
    private final TicketMachineService ticketMachineService;

    /**
     * Validates the fare collection context based on various business rules.
     *
     * @param context the fare collection validation context containing details for validation
     * @throws BusinessRuleViolationException if any validation rule is violated
     */
    public void validate(FareCollectionValidationContext context) {
        validateFinancialSanity(context);
        validateDuplicateSubmission(context.getTripExecutionId());

        TripExecution tripExecution = tripExecutionRepository.findById(context.getTripExecutionId())
                .orElseThrow(() -> new BusinessRuleViolationException("Trip execution not found"));
        TicketMachine machine = ticketMachineService.getById(context.getTicketMachineId());

        validateTripState(tripExecution);
        validateBranchBoundaries(context.getBranchId(), tripExecution, machine);
    }

    /**
     * Validates the financial details in the fare collection context.
     *
     * @param context the fare collection validation context containing financial details
     * @throws BusinessRuleViolationException if any financial value is negative
     */
    private void validateFinancialSanity(FareCollectionValidationContext context) {
        if (context.getTotalTickets() != null && context.getTotalTickets() < 0) {
            throw new BusinessRuleViolationException("Total tickets cannot be negative");
        }
        if (context.getCashCollected() != null && context.getCashCollected().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolationException("Cash collected cannot be negative");
        }
        if (context.getDigitalPayments() != null && context.getDigitalPayments().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolationException("Digital payments cannot be negative");
        }
    }

    /**
     * Validates that there is no duplicate fare collection submission for the given trip execution.
     *
     * @param tripExecutionId the ID of the trip execution to check for duplicate submissions
     * @throws BusinessRuleViolationException if a duplicate fare collection entry exists
     */
    private void validateDuplicateSubmission(Integer tripExecutionId) {
        boolean exists = fareCollectionRepository.existsByTripexecution_Id(tripExecutionId);
        if (exists) {
            throw new BusinessRuleViolationException("A fare collection entry already exists for this trip execution");
        }
    }

    /**
     * Validates the state of the trip execution to ensure it is completed.
     *
     * @param tripExecution the trip execution to validate
     * @throws BusinessRuleViolationException if the trip execution is not in a completed state
     */
    private void validateTripState(TripExecution tripExecution) {
        String state = tripExecution.getTripexecutionstatus().getName().toUpperCase();
        if (!state.equals("COMPLETED")) {
            throw new BusinessRuleViolationException("Cannot collect fare. Trip execution is currently: " + state);
        }
    }

    /**
     * Validates that the ticket machine and trip execution belong to the specified branch.
     *
     * @param counterBranchId the ID of the branch counter to validate against
     * @param tripExecution   the trip execution to validate
     * @param machine         the ticket machine to validate
     * @throws BusinessRuleViolationException if the ticket machine or trip execution belongs to a different branch
     */
    private void validateBranchBoundaries(Integer counterBranchId, TripExecution tripExecution, TicketMachine machine) {
        if (!machine.getBranch().getId().equals(counterBranchId)) {
            throw new BusinessRuleViolationException("The ticket machine does not belong to this branch counter");
        }

        if (!tripExecution.getTrip().getBranch().getId().equals(counterBranchId)) {
            throw new BusinessRuleViolationException("This trip execution belongs to a different branch registry");
        }
    }
}
