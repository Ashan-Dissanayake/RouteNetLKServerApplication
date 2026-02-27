package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.event;

import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Crewattendancestatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Tripcrewattendance;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository.CrewAttendanceStatusRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository.TripCrewAttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrewAllocationEventListener {

    private final TripRepository tripRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final TripCrewAttendanceRepository attendanceRepository;
    private final CrewAttendanceStatusRepository crewAttendanceStatusRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCrewAllocationConfirmed(CrewAllocationConfirmedEvent event) {

        if (attendanceRepository.existsByTripIdAndRoleId(
                event.getTripId(), event.getRoleId())) {
            return;
        }

        Tripcrewattendance attendance = new Tripcrewattendance();
        attendance.setTrip(tripRepository.getReferenceById(event.getTripId()));
        attendance.setRole(roleRepository.getReferenceById(event.getRoleId()));
        attendance.setPlannedemployee(
                employeeRepository.getReferenceById(event.getPlannedEmployeeId())
        );

        attendance.setActualemployee(null); // always null at creation
        attendance.setTocheckin(null);
        attendance.setTocheckout(null);

        Crewattendancestatus pending =
                crewAttendanceStatusRepository.findByName("Pending").orElseThrow();

        attendance.setCrewattendancestatus(pending);

        attendanceRepository.save(attendance);
    }
}
