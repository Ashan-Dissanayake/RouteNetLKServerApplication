package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.repository.DesignationRepository;
import lk.ashan.routenetlkserverapllication.module.roster.event.RosterShiftAssignmentEvent;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RosterShiftAutomationService {

    private final TripRepository tripRepository;
    private final ShiftRepository shiftRepository;
    private final DesignationRepository designationRepository;
    private final RosterRepository rosterRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void generateWeeklyRosterSlots(Roster roster) {
        List<Shift> activeShifts = shiftRepository.findByShiftstatus_Name("Active");
        List<Designation> targetRoles = designationRepository.findByNameIn(List.of("Driver", "Conductor"));

        LocalDate currentDay = roster.getDostartofweek();
        LocalDate endDate = roster.getDoendofweek();

        // Initialize the collection if it's null to avoid NPE
        if (roster.getRostershifts() == null) {
            roster.setRostershifts(new ArrayList<>());
        }

        while (!currentDay.isAfter(endDate)) {
            for (Shift shift : activeShifts) {

                int demandCount = (int) tripRepository.countDistinctPermitsForShift(
                        roster.getBranch().getId(),
                        shift.getTostart(),
                        shift.getToend()
                );

                int requiredCount = (demandCount > 0) ? demandCount + 1 : 0;

                for (Designation designation : targetRoles) {
                    RosterShift rostershift = new RosterShift();
                    rostershift.setShift(shift);
                    rostershift.setDesignation(designation);
                    rostershift.setDoshift(currentDay);
                    rostershift.setRequiredemployeecount(requiredCount);

                    // Use the helper method to link child to parent
                    roster.addRosterShift(rostershift);
                }
            }
            currentDay = currentDay.plusDays(1);
        }

        rosterRepository.save(roster);
        eventPublisher.publishEvent(new RosterShiftAssignmentEvent(this, roster.getId()));
    }
}
