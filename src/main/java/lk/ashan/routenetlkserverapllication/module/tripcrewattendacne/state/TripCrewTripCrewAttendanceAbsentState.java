package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.CrewAttendanceStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripCrewTripCrewAttendanceAbsentState implements TripCrewAttendanceState {

    private static final List<String> ALLOWED = List.of("PRESENT", "REPLACED");

    @Override
    public void transitionTo(TripCrewAttendance attendance, CrewAttendanceStatus newStatus) {
        String target = newStatus.getName().toUpperCase();
        if (!ALLOWED.contains(target)) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from ABSENT  to " + target
            );
        }
        attendance.setCrewattendancestatus(newStatus);
    }
}
