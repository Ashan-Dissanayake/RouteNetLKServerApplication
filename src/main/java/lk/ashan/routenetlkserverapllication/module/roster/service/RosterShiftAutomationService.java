package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.repository.DesignationRepository;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterShiftAutomationService {

    private final TripRepository tripRepository;
    private final ShiftRepository shiftRepository;
    private final DesignationRepository designationRepository;
    private final RosterRepository rosterRepository;


    @Transactional
    public void generateWeeklyRosterSlots(Roster roster) {
        List<Shift> activeShifts = shiftRepository.findByShiftstatus_Name("Active");
        List<Designation> targetRoles = designationRepository.findByNameIn(List.of("Driver", "Conductor"));

        LocalDate currentDay = roster.getDostartofweek();
        LocalDate endDate = roster.getDoendofweek();

        if (roster.getRostershifts() == null) {
            roster.setRostershifts(new ArrayList<>());
        } else {
            roster.getRostershifts().clear();
        }

        while (!currentDay.isAfter(endDate)) {
            for (Shift shift : activeShifts) {

                int demandCount = (int) tripRepository.countDistinctPermitsForShift(
                        roster.getBranch().getId(),
                        shift.getId()
                );


                int requiredCount = (demandCount > 0) ? demandCount + 1 : 0;

                for (Designation designation : targetRoles) {
                    RosterShift rostershift = new RosterShift();
                    rostershift.setShift(shift);
                    rostershift.setDesignation(designation);
                    rostershift.setDoshift(currentDay);
                    rostershift.setRequiredemployeecount(requiredCount);

                    roster.addRosterShift(rostershift);
                }
            }
            currentDay = currentDay.plusDays(1);
        }

        rosterRepository.save(roster);
    }
}
