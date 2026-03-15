package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.CrewAttendanceStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface TripCrewAttendanceState {
    void transitionTo(TripCrewAttendance employee, CrewAttendanceStatus newStatus);
    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state cannot be used as initial attendance status"
        );
    }
}
