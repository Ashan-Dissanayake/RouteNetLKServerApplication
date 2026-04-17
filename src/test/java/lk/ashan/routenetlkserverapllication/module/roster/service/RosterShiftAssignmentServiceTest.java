package lk.ashan.routenetlkserverapllication.module.roster.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private RosterAssignmentMapper mapper;
    @Mock
    private SolverManager<RosterShiftAssignmentSolution, Integer> solverManager;

    @InjectMocks
    private RosterShiftAssignmentService rosterService;

    @Test
    void generateRoster_ShouldFetchDataAndTriggerSolver() {
        // 1. Setup Mock Data
        Integer rosterId = 1;
        List<RosterShiftAssignment> mockEntities = List.of(new RosterShiftAssignment());
        List<Employee> mockEmployees = List.of(new Employee());

        List<Integer> requiredIds = List.of(1, 2);

        // 2. Define Mock Behavior
        when(assignmentRepository.findUnassignedByRosterId(rosterId)).thenReturn(mockEntities);
        when(employeeRepository.findActiveEmployeesByDesignations(requiredIds)).thenReturn(mockEmployees);

        // Mock the mapping (Simplified)
        when(mapper.toFact(any())).thenReturn(new EmployeeFact());
        when(mapper.toPlanning(any())).thenReturn(new RosterShiftAssignmentPlanning());

        // 3. Execute
        rosterService.generateRoster(rosterId);

        // 4. Verify
        // Verify that the repository was called to get the data
        verify(assignmentRepository, times(1)).findUnassignedByRosterId(rosterId);

        // Verify that the AI Solver was actually started
        verify(solverManager, times(1)).solve(
                eq(rosterId),
                any(Function.class),
                any(Consumer.class)
        );    }

}
