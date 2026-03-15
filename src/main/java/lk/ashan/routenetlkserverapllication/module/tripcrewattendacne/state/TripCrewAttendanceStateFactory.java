package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class TripCrewAttendanceStateFactory {

    private final Map<String, Supplier<TripCrewAttendanceState>> stateMap;

    public TripCrewAttendanceStateFactory() {
        stateMap = Map.of(
                "PRESENT", TripCrewTripCrewAttendancePresentState::new,
                "REPLACED", TripCrewTripCrewAttendanceReplacedState::new,
                "ABSENT", TripCrewTripCrewAttendanceAbsentState::new
        );
    }

    public TripCrewAttendanceState getState(String statusName) {
        Supplier<TripCrewAttendanceState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown crew attendance status: " + statusName);
        }
        return supplier.get();
    }
}
