package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.CrewAttendanceStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripCrewAttendanceStateTransitionHandler {

    private final TripCrewAttendanceStateFactory stateFactory;

    public void transitionTo(TripCrewAttendance attendance, CrewAttendanceStatus targetStatus) {
        String current = attendance.getCrewattendancestatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning crew attendance {} from {} to {}", attendance.getId(), current, target);

        // Exit behavior
        executeOnExit(attendance, current);

        // State validation & transition
        TripCrewAttendanceState currentState = stateFactory.getState(current);
        currentState.transitionTo(attendance, targetStatus);

        // Entry behavior
        executeOnEnter(attendance, target);
    }

    private void executeOnExit(TripCrewAttendance attendance, String statusName) {
        log.debug("Exiting {} state for crew attendance {}", statusName, attendance.getId());
    }

    private void executeOnEnter(TripCrewAttendance attendance, String statusName) {
        log.info("Entering {} state for crew attendance {}", statusName, attendance.getId());
    }
}
