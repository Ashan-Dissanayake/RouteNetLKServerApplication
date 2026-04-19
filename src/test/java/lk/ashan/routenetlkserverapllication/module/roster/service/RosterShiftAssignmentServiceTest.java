package lk.ashan.routenetlkserverapllication.module.roster.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RosterShiftAssignmentServiceTest {

    @Mock
    private RosterShiftAssignmentRepository assignmentRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private RosterAssignmentMapper mapper;
    @Mock
    private SolverManager<RosterShiftAssignmentSolution, Integer> solverManager;

    @InjectMocks
    private RosterShiftAssignmentService rosterService;

    @Test
    void generateRoster_ShouldTagInterprovincialShiftsAndSolve() {
        // 1. Setup Mock Data
        Integer rosterId = 1;
        LocalDate testDate = LocalDate.now();

        // Create a real-ish hierarchy for the Stream logic to work
        Shift shift = new Shift();
        shift.setTostart(LocalTime.of(8, 0));
        shift.setToend(LocalTime.of(16, 0));

        RosterShift rs = new RosterShift();
        rs.setDoshift(testDate);
        rs.setShift(shift);

        RosterShiftAssignment rsa = new RosterShiftAssignment();
        rsa.setRostershift(rs);
        List<RosterShiftAssignment> mockEntities = List.of(rsa);

        // Mock an Interprovincial Trip that overlaps with the shift (10:00 AM)
        Trip interTrip = new Trip();
        interTrip.setTodepature(LocalTime.of(10, 0));
        interTrip.setDoservice(testDate);

        // 2. Define Mock Behavior
        when(assignmentRepository.findUnassignedByRosterId(rosterId)).thenReturn(mockEntities);
        when(tripRepository.findInterprovincialTrips(testDate)).thenReturn(List.of(interTrip));

        // Mock Employee data
        Employee emp = new Employee();
        Branch b = new Branch(); b.setId(1);
        emp.setBranch(b);
        when(employeeRepository.findActiveEmployeesByDesignations(any())).thenReturn(List.of(emp));

        when(mapper.toFact(any())).thenReturn(new EmployeeFact());
        when(mapper.toPlanning(any())).thenReturn(new RosterShiftAssignmentPlanning());

        // 3. Execute
        rosterService.generateRoster(rosterId);

        // 4. Verify
        // Verify trip repository was checked for interprovincial demand
        verify(tripRepository).findInterprovincialTrips(testDate);

        // Verify shift was tagged as level 2 (High Familiarity)
        assertEquals(2, rs.getRequiredFamiliarityLevel());

        // Verify Solver was started
        verify(solverManager).solve(eq(rosterId),
                any(Function.class),
                any(Consumer.class)
        );
    }
}
