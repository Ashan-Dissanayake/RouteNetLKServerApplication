package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceStateFactory {

    private final PendingAttendanceState pendingState;
    private final PresentAttendanceState presentState;

    public AttendanceState getState(String statusName) {

        return switch (statusName.toUpperCase()) {
            case "PENDING" -> pendingState;
            case "PRESENT", "REPLACED", "ABSENT" -> presentState;
            default -> throw new IllegalStateException("Unknown status");
        };
    }
}
