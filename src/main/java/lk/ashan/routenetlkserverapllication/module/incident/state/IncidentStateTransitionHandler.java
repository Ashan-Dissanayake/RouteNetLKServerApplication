package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles state transitions for incidents, including entry and exit behaviors.
 * This class is a Spring component and uses Lombok annotations for logging and constructor injection.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentStateTransitionHandler {

    private final IncidentStatusFactory incidentStatusFactory;

    /**
     * Transitions an incident to a new status, executing exit and entry behaviors as needed.
     *
     * @param incident   The incident to transition.
     * @param newStatus  The new status to transition the incident to.
     * @throws IllegalArgumentException if the transition is invalid.
     */
    public void transitionTo(Incident incident, IncidentStatus newStatus) {
        String currentStatus = incident.getIncidentstatus().getName();
        String targetStatus = newStatus.getName();

        // Exit behavior
        executeOnExit(incident, currentStatus);

        // Validate transition
        IncidentState currentState = incidentStatusFactory.getState(currentStatus);
        currentState.transitionTo(incident, newStatus);

        // Entry behavior
        executeOnEnter(incident, targetStatus);
    }

    /**
     * Executes the exit behavior for the given incident and status.
     *
     * @param incident   The incident for which the exit behavior is executed.
     * @param statusName The name of the current status being exited.
     */
    private void executeOnExit(Incident incident, String statusName) {
        switch (statusName.toUpperCase()) {
            case "REPORTED" -> log.debug("Exiting REPORTED state for incident {}", incident.getId());
            case "IN_PROGRESS" -> log.debug("Exiting IN_PROGRESS state for incident {}", incident.getId());
            case "RESOLVED" -> log.debug("Exiting RESOLVED state for incident {}", incident.getId());
        }
    }

    /**
     * Executes the entry behavior for the given incident and status.
     *
     * @param incident   The incident for which the entry behavior is executed.
     * @param statusName The name of the target status being entered.
     */
    private void executeOnEnter(Incident incident, String statusName) {
        switch (statusName.toUpperCase()) {
            case "REPORTED" -> log.info("Entering REPORTED state for incident {}", incident.getId());
            case "IN_PROGRESS" -> log.info("Entering IN_PROGRESS state for incident {}", incident.getId());
            case "RESOLVED" -> log.info("Entering RESOLVED state for incident {}", incident.getId());
            case "CLOSED" -> log.info("Entering CLOSED state for incident {}", incident.getId());
        }
    }
}
