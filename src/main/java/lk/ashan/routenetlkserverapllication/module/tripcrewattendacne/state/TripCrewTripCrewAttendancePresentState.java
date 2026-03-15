package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.CrewAttendanceStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripCrewTripCrewAttendancePresentState implements TripCrewAttendanceState {

    private static final List<String> ALLOWED = List.of("REPLACED", "ABSENT");

    @Override
    public void transitionTo(TripCrewAttendance attendance, CrewAttendanceStatus newStatus) {
        String target = newStatus.getName().toUpperCase();
        if (!ALLOWED.contains(target)) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from PRESENT to " + target
            );
        }
        attendance.setCrewattendancestatus(newStatus);
    }

    @Override
    public void validateInitial() {
        // PRESENT can be an initial state
    }
}
