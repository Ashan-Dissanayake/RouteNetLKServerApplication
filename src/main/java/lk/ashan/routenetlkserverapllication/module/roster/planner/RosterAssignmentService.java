package lk.ashan.routenetlkserverapllication.module.roster.planner;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterAssignmentStatusRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterAssignmentService {

    private final EmployeeRepository employeeRepository;
    private final RosterRepository rosterRepository;
    private final RosterAssignmentRepository rosterAssignmentRepository;
    private final RosterStatusRepository rosterStatusRepository;
    private final RosterAssignmentStatusRepository rosterAssignmentStatusRepository;
    private final EmployeePlanningMapper employeePlanningMapper;
    private final SolverFactory<RosterAssignmentSolution> solverFactory;

    /**
     * Load eligible employees for roster assignment.
     * Only loads drivers (designation 1) and conductors (designation 2)
     * who are active and belong to the specified branch.
     */
    public List<Employee> loadEligibleEmployees(Integer branchId, String employeeStatus) {
        log.info("Loading eligible employees for branch {} with status {}", branchId, employeeStatus);

        List<Integer> designationIds = List.of(1, 2); // 1=Driver, 2=Conductor

        List<Employee> employees = employeeRepository
                .findByDeletedFalseAndEmployeestatus_NameAndBranch_IdAndDesignation_IdIn(
                        employeeStatus,
                        branchId,
                        designationIds
                );

        log.info("Found {} eligible employees", employees.size());
        return employees;
    }

    /**
     * Load planned rosters for a specific branch and date.
     * Only loads rosters with status "Planned" that need assignments.
     */
    public List<Roster> loadPlannedRosters(Integer branchId, LocalDate date) {
        log.info("Loading planned rosters for branch {} on date {}", branchId, date);

        List<Roster> rosters = rosterRepository
                .findByBranch_IdAndDorosterAndRosterstatus_Name(
                        branchId,
                        date,
                        "Draft"
                );

        log.info("Found {} draft rosters", rosters.size());
        return rosters;
    }

    /**
     * Map JPA entities to OptaPlanner planning model.
     * Creates assignment slots (2 per roster: 1 driver + 1 conductor).
     */
    public RosterAssignmentSolution mapToPlanningModel(
            List<Roster> rosters,
            List<EmployeePlanning> employees
    ) {
        log.info("Mapping {} rosters and {} employees to planning model",
                rosters.size(), employees.size());

        RosterAssignmentSolution solution = new RosterAssignmentSolution();
        solution.setAllEmployees(employees);

        List<RosterAssignmentPlanning> assignments = new ArrayList<>();
        AtomicInteger idCounter = new AtomicInteger(0);

        for (Roster roster : rosters) {
            // Create driver slot
            RosterAssignmentPlanning driverSlot = new RosterAssignmentPlanning();
            driverSlot.setId("roster-" + roster.getId() + "-driver-" + idCounter.incrementAndGet());
            driverSlot.setRosterId(roster.getId());
            driverSlot.setRequiredDesignationId(1); // Driver
            driverSlot.setBranchId(roster.getBranch().getId());
            driverSlot.setShiftId(roster.getShift().getId().toString());
            driverSlot.setRosterDate(roster.getDoroster());
            driverSlot.setEmployee(null); // Unassigned initially
            assignments.add(driverSlot);

            // Create conductor slot
            RosterAssignmentPlanning conductorSlot = new RosterAssignmentPlanning();
            conductorSlot.setId("roster-" + roster.getId() + "-conductor-" + idCounter.incrementAndGet());
            conductorSlot.setRosterId(roster.getId());
            conductorSlot.setRequiredDesignationId(2); // Conductor
            conductorSlot.setBranchId(roster.getBranch().getId());
            conductorSlot.setShiftId(roster.getShift().getId().toString());
            conductorSlot.setRosterDate(roster.getDoroster());
            conductorSlot.setEmployee(null); // Unassigned initially
            assignments.add(conductorSlot);
        }

        solution.setAssignmentList(assignments);
        log.info("Created {} assignment slots", assignments.size());

        return solution;
    }

    /**
     * Solve the roster assignment problem using OptaPlanner.
     * Returns an optimized solution with employees assigned to rosters.
     */
    public RosterAssignmentSolution solve(RosterAssignmentSolution problem) {
        log.info("Starting OptaPlanner solver...");
        log.info("Problem size: {} assignments, {} employees",
                problem.getAssignmentList().size(),
                problem.getAllEmployees().size());

        Solver<RosterAssignmentSolution> solver = solverFactory.buildSolver();
        RosterAssignmentSolution solution = solver.solve(problem);

        log.info("Solver finished!");
        log.info("Final score: {}", solution.getScore());
        log.info("Assigned: {}/{} slots",
                solution.getAssignmentCount(),
                solution.getTotalSlots());

        return solution;
    }

    /**
     * Validate the solution before persisting.
     * Checks for common issues like unassigned slots or constraint violations.
     */
    public void validateSolution(RosterAssignmentSolution solution) {
        log.info("Validating solution...");

        // Check for unassigned slots
        List<RosterAssignmentPlanning> unassigned = solution.getUnassignedSlots();
        if (!unassigned.isEmpty()) {
            log.warn("Found {} unassigned slots", unassigned.size());
            unassigned.forEach(slot ->
                    log.warn("Unassigned: Roster {} - {}",
                            slot.getRosterId(),
                            slot.isDriverSlot() ? "Driver" : "Conductor")
            );
        }

        // Check for hard constraint violations
        if (solution.getScore() != null && solution.getScore().hardScore() < 0) {
            log.warn("Solution has hard constraint violations: {}", solution.getScore());
        }

        log.info("Validation complete");
    }

    /**
     * Persist the optimized assignments to the database.
     * Updates roster status to "Solved" after successful persistence.
     */
    @Transactional
    public void persistAssignments(
            RosterAssignmentSolution solution,
            List<Employee> originalEmployees
    ) {
        log.info("Persisting assignments to database...");

        // Create employee lookup map
        Map<Integer, Employee> employeeMap = originalEmployees.stream()
                .collect(Collectors.toMap(Employee::getId, e -> e));

        // Get roster lookup map
        List<Integer> rosterIds = solution.getAssignmentList().stream()
                .map(RosterAssignmentPlanning::getRosterId)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, Roster> rosterMap = rosterRepository.findAllById(rosterIds).stream()
                .collect(Collectors.toMap(Roster::getId, r -> r));

        // Get status entities
        Rosterassignementstatus assignedStatus =
                rosterAssignmentStatusRepository.findByName("Assigned");
        Rosterstatus solvedStatus =
                rosterStatusRepository.findByName("Solved");

        if (assignedStatus == null) {
            throw new IllegalStateException("Assignment status 'Assigned' not found in database");
        }
        if (solvedStatus == null) {
            throw new IllegalStateException("Roster status 'Solved' not found in database");
        }

        // Create assignment entities
        List<Rosterassignement> assignmentsToSave = new ArrayList<>();
        int assignedCount = 0;

        for (RosterAssignmentPlanning slot : solution.getAssignmentList()) {
            if (slot.getEmployee() != null) {
                Employee employee = employeeMap.get(slot.getEmployee().getId());
                Roster roster = rosterMap.get(slot.getRosterId());
                if (employee != null && roster != null) {
                    Rosterassignement assignment = new Rosterassignement();
                    assignment.setRoster(roster);
                    assignment.setEmployee(employee);
                    assignment.setRosterassignementstatus(assignedStatus);
                    assignmentsToSave.add(assignment);
                    assignedCount++;
                } else {
                    log.warn("Could not find employee {} or roster {} for assignment",
                            slot.getEmployee().getId(), slot.getRosterId());
                }
            }
        }

        // Save all assignments
        rosterAssignmentRepository.saveAll(assignmentsToSave);
        log.info("Saved {} roster assignments", assignedCount);

        // Update roster statuses
        rosterMap.values().forEach(roster -> roster.setRosterstatus(solvedStatus));
        rosterRepository.saveAll(rosterMap.values());
        log.info("Updated {} rosters to 'Solved' status", rosterMap.size());
    }

    /**
     * Complete workflow: load, solve, validate, and persist roster assignments.
     */
    @Transactional
    public RosterAssignmentSolution executeAssignment(Integer branchId, LocalDate date) {
        log.info("=== Starting roster assignment for branch {} on {} ===", branchId, date);

        try {
            // Step 1: Load employees
            List<Employee> employees = loadEligibleEmployees(branchId, "Active");
            if (employees.isEmpty()) {
                log.error("No eligible employees found for branch {}", branchId);
                throw new IllegalStateException("No eligible employees available");
            }

            // Step 2: Load rosters
            List<Roster> rosters = loadPlannedRosters(branchId, date);
            if (rosters.isEmpty()) {
                log.warn("No planned rosters found for branch {} on {}", branchId, date);
                return null;
            }

            // Step 3: Map to planning model
            List<EmployeePlanning> planningEmployees =
                    employeePlanningMapper.toEmployeePlanningList(employees);
            RosterAssignmentSolution problem =
                    mapToPlanningModel(rosters, planningEmployees);

            log.info("Starting solver with {} employees and {} slots",
                    problem.getEmployeeRange().size(),
                    problem.getAssignmentList().size());

            // Step 4: Solve
            RosterAssignmentSolution solution = solve(problem);

            log.info("Solver finished. Score: {}", solution.getScore());
            log.info("Assigned slots: {}", solution.getAssignmentList().stream()
                    .filter(slot -> slot.getEmployee() != null).count());

            // Step 5: Validate
            validateSolution(solution);

            // Step 6: Persist
            persistAssignments(solution, employees);

            log.info("=== Roster assignment completed successfully ===");
            return solution;

        } catch (Exception e) {
            log.error("Error during roster assignment", e);
            throw new RuntimeException("Roster assignment failed: " + e.getMessage(), e);
        }
    }
}
