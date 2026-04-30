package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.repository.DesignationRepository;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class RosterShiftAutomationServiceTest {

    @Mock private TripRepository tripRepository;
    @Mock private RosterRepository rosterRepository;
    @Mock private ShiftRepository shiftRepository;
    @Mock private DesignationRepository designationRepository;

    @InjectMocks
    private RosterShiftAutomationService rosterAutomationService;

    private Roster testRoster;
    private Shift morningShift;
    private Designation driver;
    private Designation conductor;

    @BeforeEach
    void setUp() {
        // Setup a 7-day Roster
        Branch branch = new Branch();
        branch.setId(1);

        testRoster = new Roster();
        testRoster.setId(100);
        testRoster.setBranch(branch);
        testRoster.setDostartofweek(LocalDate.of(2026, 5, 4)); // A Monday
        testRoster.setDoendofweek(LocalDate.of(2026, 5, 10));  // Sunday

        // Setup Shift
        morningShift = new Shift();
        morningShift.setName("Morning");
        morningShift.setTostart(LocalTime.of(6, 0));
        morningShift.setToend(LocalTime.of(14, 0));

        // Setup Designations
        driver = new Designation(); driver.setName("Driver");
        conductor = new Designation(); conductor.setName("Conductor");
    }

    @Test
    @DisplayName("Should populate Roster with 14 shifts and save once")
    void testGenerateWeeklyRosterSlots_Success() {
        // GIVEN
        when(shiftRepository.findByShiftstatus_Name("Active")).thenReturn(List.of(morningShift));
        when(designationRepository.findByNameIn(anyList())).thenReturn(List.of(driver, conductor));
        when(tripRepository.countDistinctPermitsForShift(anyInt(), any(), any())).thenReturn(5L);

        // WHEN
        rosterAutomationService.generateWeeklyRosterSlots(testRoster);

        // THEN
        // 1. Verify the parent repository was saved exactly once
        // Note the closing parenthesis after times(1)
        verify(rosterRepository, times(1)).save(testRoster);

        // 2. Verify the collection size inside the Roster object
        assertNotNull(testRoster.getRostershifts(), "Shift collection should not be null");
        assertEquals(14, testRoster.getRostershifts().size(), "Should have 14 shifts (7 days * 2 roles)");

        // 3. Verify the data integrity of a sample shift
        RosterShift sampleShift = testRoster.getRostershifts().stream().findFirst().orElseThrow();
        assertEquals(6, sampleShift.getRequiredemployeecount(), "Demand 5 + Buffer 1 should be 6");
        assertEquals(testRoster, sampleShift.getRoster(), "The back-reference to Roster must be set for JPA cascading");

        // 3. Verify the data inside the collection
        RosterShift firstShift = testRoster.getRostershifts().iterator().next();
        assertEquals(6, firstShift.getRequiredemployeecount(), "Demand 5 + Buffer 1 should be 6");
        assertEquals(testRoster, firstShift.getRoster(), "Back-reference to parent must be set");
    }

    @Test
    @DisplayName("Should handle zero demand correctly")
    void testGenerateWeeklyRosterSlots_NoDemand() {
        // GIVEN
        when(shiftRepository.findByShiftstatus_Name("Active")).thenReturn(List.of(morningShift));
        when(designationRepository.findByNameIn(anyList())).thenReturn(List.of(driver, conductor));
        when(tripRepository.countDistinctPermitsForShift(anyInt(), any(), any())).thenReturn(0L);

        // WHEN
        rosterAutomationService.generateWeeklyRosterSlots(testRoster);

        // THEN
        verify(rosterRepository, times(1)).save(testRoster);

        // Pick any shift and verify count is 0
        int count = testRoster.getRostershifts().iterator().next().getRequiredemployeecount();
        assertEquals(0, count, "Required count should be 0 when no trips exist");
    }

}
