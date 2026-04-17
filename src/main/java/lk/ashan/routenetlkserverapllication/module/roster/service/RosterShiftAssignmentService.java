package lk.ashan.routenetlkserverapllication.module.roster.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDTO;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment.RosterShiftAssignmentStateTransitionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterShiftAssignmentService {

    private final RosterShiftAssignmentRepository assignmentRepository;
    private final RosterShiftAssignmentStatusService rosterShiftAssignmentStatusService;
    private final EmployeeRepository employeeRepository;
    private final RosterAssignmentMapper mapper;

    private final SolverManager<RosterShiftAssignmentSolution, Integer> solverManager;
    private final RosterShiftAssignmentStateTransitionHandler rosterShiftAssignmentStateTransitionHandler;

    @Transactional(readOnly = true)
    public List<RosterShiftAssignmentResponseDTO> getAssignmentsByRosterId(Integer rosterId) {
        List<RosterShiftAssignment> assignments = assignmentRepository.findByRosterId(rosterId);
        return mapper.toDtoList(assignments);
    }

    @Transactional
    public void generateRoster(Integer rosterId) {
        List<RosterShiftAssignment> entities = assignmentRepository.findUnassignedByRosterId(rosterId);

        List<Integer> requiredIds = List.of(1, 2);
        List<Employee> employeeEntities = employeeRepository.findActiveEmployeesByDesignations(requiredIds);

        List<EmployeeFact> employeeFacts = employeeEntities.stream()
                .map(mapper::toFact)
                .toList();

        List<RosterShiftAssignmentPlanning> planningEntities = entities.stream()
                .map(mapper::toPlanning)
                .toList();

        RosterShiftAssignmentSolution problem = new RosterShiftAssignmentSolution(
                employeeFacts,
                planningEntities
        );

        log.info("Employees passed to solver: " + employeeFacts.size());
        for(EmployeeFact f : employeeFacts) {
            log.info("Emp: " + f.getFullname() + " | Desig: " + f.getDesignationId());
        }

        for(RosterShiftAssignmentPlanning p : planningEntities) {
            log.info("Shift ID: " + p.getId() + " | Needs Desig: " + p.getDesignationId());
        }

        // 5. Start Solving
        // rosterId is used as the problemId to track the task
        solverManager.solve(rosterId,
                (Integer id) -> problem,
                this::saveResult
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveResult(RosterShiftAssignmentSolution solution) {
        log.info("--- PERSISTENCE START (DIRECT UPDATE) ---");

        // 1. Get the Proposed Status ID (Assuming it's 2, but fetching dynamically is safer)
        Integer proposedStatusId = rosterShiftAssignmentStatusService.getByName("Proposed").getId();

        for (RosterShiftAssignmentPlanning planning : solution.getAssignmentList()) {
            if (planning.getEmployeeFact() != null) {

                // 2. Call the new combined update method
                assignmentRepository.updateEmployeeAndStatusDirectly(
                        planning.getId(),
                        planning.getEmployeeFact().getId(),
                        proposedStatusId
                );

                log.info("SUCCESS: Assignment {} set to Employee {} with status PROPOSED",
                        planning.getId(), planning.getEmployeeFact().getId());
            }
        }
        log.info("--- PERSISTENCE COMPLETE ---");
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void saveResult(RosterShiftAssignmentSolution solution) {
//        for (RosterShiftAssignmentPlanning planning : solution.getAssignmentList()) {
//            if (planning.getEmployeeFact() != null) {
//               assignmentRepository.updateEmployeeDirectly(planning.getId(), planning.getEmployeeFact().getId());
//                log.info("FORCE UPDATED: ID " + planning.getId());
//            }
//        }
//    }
}
