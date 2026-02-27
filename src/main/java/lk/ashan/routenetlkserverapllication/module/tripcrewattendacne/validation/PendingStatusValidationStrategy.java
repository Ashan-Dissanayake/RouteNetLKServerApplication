package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class PendingStatusValidationStrategy implements CrewCheckInValidationStrategy {

    @Override
    public void validate(CrewAttendanceContext context) {

        String status = context.getAttendance()
                .getCrewattendancestatus()
                .getName();

        if (!"PENDING".equalsIgnoreCase(status)) {
            throw new InvalidStateTransitionException(
                    "Check-in allowed only when status is PENDING"
            );
        }
    }
}
