package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterAssignmentSolverService {

    private final SolverFactory<RosterScheduleSolution> solverFactory;

    /**
     * Generate optimal assignments for all shifts in roster
     *
     * @param roster The roster (week) to plan
     * @param shifts All shifts that need staffing this week
     * @param roles Required roles per shift
     * @param candidateEmployees Available employees
     * @param existingAssignments Already confirmed assignments (constraints)
     * @return Optimized solution with suggested assignments
     */
    public RosterScheduleSolution solve(
            Roster roster,
            List<Shift> shifts,
            List<Role> roles,
            List<Employee> candidateEmployees,
            List<Shiftrosterassignment> existingAssignments) {

        log.info("========== ROSTER SOLVER START ==========");
        log.info("Roster ID: {}", roster.getId());
        log.info("Week: {} to {}", roster.getDostartofweek(), roster.getDoendofweek());
        log.info("Branch: {}", roster.getBranch().getName());

        // 1. Convert Employee entities → EmployeeFact (problem facts)
        List<EmployeeFact> employeeFacts = candidateEmployees.stream()
                .map(this::convertToEmployeeFact)
                .toList();

        log.info("Available employees: {}", employeeFacts.size());
        employeeFacts.forEach(e ->
                log.debug("  Employee: {} - Branch: {}, Roles: {}, Rate: {}",
                        e.getNumber(), e.getBranchId(), e.getQualifiedRoles(), e.getHourlyRate())
        );

        // 2. Create planning entities (assignments to optimize)
        List<RosterAssignmentPlanning> planningAssignments =
                createPlanningAssignments(roster, shifts, roles);

        log.info("Planning entities created: {}", planningAssignments.size());
        planningAssignments.forEach(a ->
                log.debug("  Assignment: Shift={}, Role={}, Date={}",
                        a.getShift().getName(), a.getRole().getName(), a.getDoassigned())
        );

        // 3. Build problem (solution to optimize)
        RosterScheduleSolution problem = new RosterScheduleSolution(
                planningAssignments,
                employeeFacts,
                existingAssignments,
                null  // Score calculated by solver
        );

        log.info("Problem built: {} assignments, {} employees, {} existing assignments",
                problem.getAssignmentCount(),
                problem.getEmployeeCount(),
                existingAssignments != null ? existingAssignments.size() : 0
        );

        // 4. Solve
        log.info("Starting solver...");
        Solver<RosterScheduleSolution> solver = solverFactory.buildSolver();
        RosterScheduleSolution solved = solver.solve(problem);

        log.info("========== ROSTER SOLVER END ==========");
        log.info("Best score: {}", solved.getScore());
        log.info("Feasible: {}", solved.isFeasible());

        // 5. Log unassigned entities
        long unassigned = solved.getAssignments().stream()
                .filter(a -> a.getAssignedEmployee() == null)
                .count();

        if (unassigned > 0) {
            log.warn("{} assignment(s) could not be filled", unassigned);
            solved.getAssignments().stream()
                    .filter(a -> a.getAssignedEmployee() == null)
                    .forEach(a -> log.warn("  Unfilled: Shift={}, Role={}, Date={}",
                            a.getShift().getName(), a.getRole().getName(), a.getDoassigned())
                    );
        } else {
            log.info("All assignments filled successfully!");
        }

        return solved;
    }

    /**
     * Convert Employee entity to EmployeeFact for OptaPlanner
     */
    private EmployeeFact convertToEmployeeFact(Employee employee) {

        EmployeeFact fact = new EmployeeFact();
        fact.setId(employee.getId());
        fact.setNumber(employee.getNumber());
        fact.setFullname(employee.getFullname());
        fact.setBranchId(employee.getBranch().getId());
        fact.setStatus(employee.getEmployeestatus().getName());

        // Extract qualified roles
        List<Integer> qualifiedRoles = new ArrayList<>();

        // Check if employee is a driver
        if (employee.getDriver() != null &&
                "Eligible".equalsIgnoreCase(employee.getDriver().getCrewstatus().getName())) {
            qualifiedRoles.add(1);  // Assuming role_id=1 is Driver
        }

        // Check if employee is a conductor
        if (employee.getConductor() != null &&
                "Eligible".equalsIgnoreCase(employee.getConductor().getCrewstatus().getName())) {
            qualifiedRoles.add(2);  // Assuming role_id=2 is Conductor
        }

        fact.setQualifiedRoles(qualifiedRoles);

        // Set default values (enhance later with real data)
        fact.setPreferredShiftIds(new ArrayList<>());  // No preferences initially
        fact.setHourlyRate(1500);  // Default rate (enhance with real salary data)

        // Calculate experience years from date of joining
        if (employee.getDoj() != null) {
            int years = java.time.Period.between(
                    employee.getDoj(),
                    java.time.LocalDate.now()
            ).getYears();
            fact.setExperienceYears(years);
        } else {
            fact.setExperienceYears(0);
        }

        fact.setUnavailableDaysOfWeek(new ArrayList<>());
        fact.setPreferredMaxHoursPerWeek(48);  // Default max

        return fact;
    }

    /**
     * Create planning entities - one for each shift/role/date combination
     * Example:
     * - Morning shift needs Driver and Conductor
     * - Week is Mon-Sun (7 days)
     * - Creates 14 planning entities (2 roles × 7 days)
     */
    private List<RosterAssignmentPlanning> createPlanningAssignments(
            Roster roster,
            List<Shift> shifts,
            List<Role> roles) {

        List<RosterAssignmentPlanning> assignments = new ArrayList<>();
        int idCounter = 1;

        // For each day in the roster week
        LocalDate currentDate = roster.getDostartofweek();
        while (!currentDate.isAfter(roster.getDoendofweek())) {

            // For each shift
            for (Shift shift : shifts) {

                // For each role needed in this shift
                for (Role role : roles) {

                    RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
                    assignment.setId(idCounter++);
                    assignment.setRoster(roster);
                    assignment.setShift(shift);
                    assignment.setRole(role);
                    assignment.setDoassigned(currentDate);
                    assignment.setAssignedEmployee(null);  // OptaPlanner fills this

                    assignments.add(assignment);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return assignments;
    }
}

