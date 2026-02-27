package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CrewAllocationConfirmedEvent {
    private final Integer tripId;
    private final Integer roleId;
    private final Integer plannedEmployeeId;
}
