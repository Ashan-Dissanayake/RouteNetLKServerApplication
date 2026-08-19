package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles state transitions for Employee entities.
 * This class is responsible for managing the transition between different states
 * of an Employee, including executing entry and exit behaviors.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeStateTransitionHandler {

    private final EmployeeStateFactory employeeStateFactory;

    /**
     * Transitions an Employee to a target status.
     *
     * @param employee     The Employee entity to transition.
     * @param targetStatus The target status to transition the Employee to.
     * @throws IllegalArgumentException if the transition is invalid.
     */
    public void transitionTo(Employee employee, EmployeeStatus targetStatus) {
        String currentStatus = employee.getEmployeestatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning branch {} from {} to {}", employee.getId(), currentStatus, target);

        // Exit behavior
        executeOnExit(employee, currentStatus);

        // Validate & transition
        EmployeeState currentState = employeeStateFactory.getState(currentStatus);
        currentState.transitionTo(employee, targetStatus);

        // Entry behavior
        executeOnEnter(employee, target);
    }

    /**
     * Executes exit behavior for the given Employee and status.
     *
     * @param employee   The Employee entity exiting the state.
     * @param statusName The name of the state being exited.
     */
    private void executeOnExit(Employee employee, String statusName) {
        log.debug("Exiting {} state for branch {}", statusName, employee.getId());
    }

    /**
     * Executes entry behavior for the given Employee and status.
     *
     * @param employee   The Employee entity entering the state.
     * @param statusName The name of the state being entered.
     */
    private void executeOnEnter(Employee employee, String statusName) {
        log.info("Entering {} state for branch {}", statusName, employee.getId());
    }
}
