package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeStateTransitionHandler {

    private final EmployeeStateFactory employeeStateFactory;

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

    private void executeOnExit(Employee employee, String statusName) {
        log.debug("Exiting {} state for branch {}", statusName, employee.getId());
    }

    private void executeOnEnter(Employee employee, String statusName) {
        log.info("Entering {} state for branch {}", statusName, employee.getId());
    }
}
