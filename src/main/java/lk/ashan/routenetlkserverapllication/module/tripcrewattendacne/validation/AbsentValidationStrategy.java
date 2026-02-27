package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Tripcrewattendance;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class AbsentValidationStrategy {

    public void validate(Tripcrewattendance attendance) {

        String status = attendance.getCrewattendancestatus().getName();

        if (!"PENDING".equalsIgnoreCase(status)) {
            throw new InvalidStateTransitionException(
                    "Only PENDING attendance can be marked ABSENT"
            );
        }
    }
}
