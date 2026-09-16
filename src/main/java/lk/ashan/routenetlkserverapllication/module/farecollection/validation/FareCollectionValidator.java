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

@Component
@RequiredArgsConstructor
public class FareCollectionValidator {

    private final FareCollectionRepository fareCollectionRepository;
    private final TripExecutionRepository tripExecutionRepository;
    private final TicketMachineService ticketMachineService;

    public void validateCreate(
            Integer branchId,
            Integer tripExecutionId,
            Integer ticketMachineId,
            Integer totalTickets,
            BigDecimal cashCollected,
            BigDecimal digitalPayments
    ) {

        validateFinancialSanity(totalTickets, cashCollected, digitalPayments);

        validateDuplicateSubmission(tripExecutionId);

        TripExecution tripExecution =
                tripExecutionRepository.findById(tripExecutionId)
                        .orElseThrow(() ->
                                new BusinessRuleViolationException(
                                        "Trip execution not found"
                                ));

        TicketMachine machine = ticketMachineService.getById(ticketMachineId);

        validateTripState(tripExecution);
        validateBranchBoundaries(branchId, tripExecution, machine);
    }

    private void validateFinancialSanity(Integer totalTickets, BigDecimal cashCollected, BigDecimal digitalPayments) {

        if (totalTickets != null && totalTickets < 0) {
            throw new BusinessRuleViolationException(
                    "Total tickets cannot be negative"
            );
        }

        if (cashCollected != null && cashCollected.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolationException(
                    "Cash collected cannot be negative"
            );
        }

        if (digitalPayments != null && digitalPayments.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolationException(
                    "Digital payments cannot be negative"
            );
        }
    }

    private void validateDuplicateSubmission(Integer tripExecutionId) {

        if (fareCollectionRepository.existsByTripexecution_Id(tripExecutionId)) {
            throw new BusinessRuleViolationException(
                    "A fare collection entry already exists for this trip execution"
            );
        }
    }

    private void validateTripState(TripExecution tripExecution) {

        String state = tripExecution
                        .getTripexecutionstatus()
                        .getName()
                        .toUpperCase();

        if (!"COMPLETED".equals(state)) {
            throw new BusinessRuleViolationException(
                    "Cannot collect fare. Trip execution is currently: "
                            + state
            );
        }
    }

    private void validateBranchBoundaries(Integer branchId, TripExecution tripExecution, TicketMachine machine) {

        if (!machine.getBranch().getId().equals(branchId)) {
            throw new BusinessRuleViolationException(
                    "The ticket machine does not belong to this branch counter"
            );
        }

        if (!tripExecution.getTrip().getBranch().getId().equals(branchId)) {
            throw new BusinessRuleViolationException(
                    "This trip execution belongs to a different branch registry"
            );
        }
    }
}