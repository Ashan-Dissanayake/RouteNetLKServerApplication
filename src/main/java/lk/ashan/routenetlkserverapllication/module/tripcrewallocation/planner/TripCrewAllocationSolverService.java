package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.planner;

//import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
        import org.springframework.stereotype.Service;

/**
 * OptaPlanner solver service for Trip Crew Allocation.
 * UPDATED VERSION: Properly loads and converts employee qualifications
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TripCrewAllocationSolverService {

//    private final SolverFactory<TripCrewScheduleSolution> solverFactory;
//    private final EmployeeRepository employeeRepository;
//
//    /**
//     * Generate optimal crew assignments for a single trip.
//     */
//    public TripCrewScheduleSolution solve(
//            Trip trip,
//            Shift derivedShift,
//            List<Role> roles,
//            List<Employee> candidateEmployees,
//            List<Rostershiftassignment> confirmedRosterAssignments,
//            List<Tripcrewallocation> existingAllocations) {
//
//        log.info("========== TRIP CREW ALLOCATION SOLVER START ==========");
//        log.info("Trip ID: {}", trip.getId());
//        log.info("Service Date: {}", trip.getDoservice());
//        log.info("Departure: {} - Arrival: {}", trip.getTodepature(), trip.getToarrival());
//        log.info("Derived Shift: {} ({} - {})",
//                derivedShift.getName(),
//                derivedShift.getTostart(),
//                derivedShift.getToend());
//        log.info("Branch: {}", trip.getBranch().getName());
//        log.info("Route: {}", trip.getPermite().getRoute().getNumber());
//
//        // CRITICAL: Reload employees with eager fetching to avoid lazy loading issues
//        List<Integer> employeeIds = candidateEmployees.stream()
//                .map(Employee::getId)
//                .collect(Collectors.toList());
//
//        List<Employee> employeesWithQualifications =
//                employeeRepository.findByIdInWithCrewData(employeeIds);
//
//        log.info("Reloaded {} employees with crew qualifications", employeesWithQualifications.size());
//
//        // 1. Convert Employee entities → EmployeeFact (problem facts)
//        List<EmployeeFact> employeeFacts = employeesWithQualifications.stream()
//                .map(this::convertToEmployeeFact)
//                .toList();
//
//        log.info("Available employees: {}", employeeFacts.size());
//        employeeFacts.forEach(e ->
//                log.info("  Employee: {} - Roles: {}, License: {}, Familiarity: {}",
//                        e.getNumber(),
//                        e.getQualifiedRoles(),
//                        e.getLicenseCategoryId(),
//                        e.getRouteFamiliarityLevelId())
//        );
//
//        // 2. Create planning entities (assignments to optimize)
//        List<TripCrewAssignmentPlanning> planningAssignments =
//                createPlanningAssignments(trip, derivedShift, roles);
//
//        log.info("Planning entities created: {} (need to fill {} roles)",
//                planningAssignments.size(), roles.size());
//
//        // 3. Build problem (solution to optimize)
//        TripCrewScheduleSolution problem = new TripCrewScheduleSolution(
//                planningAssignments,
//                employeeFacts,
//                confirmedRosterAssignments,
//                existingAllocations,
//                null  // Score calculated by solver
//        );
//
//        log.info("Problem built: {} assignments, {} employees, {} roster confirmations, {} existing allocations",
//                problem.getAssignmentCount(),
//                employeeFacts.size(),
//                confirmedRosterAssignments.size(),
//                existingAllocations != null ? existingAllocations.size() : 0
//        );
//
//        // 4. Solve
//        log.info("Starting solver...");
//        Solver<TripCrewScheduleSolution> solver = solverFactory.buildSolver();
//        TripCrewScheduleSolution solved = solver.solve(problem);
//
//        log.info("========== TRIP CREW ALLOCATION SOLVER END ==========");
//        log.info("Best score: {}", solved.getScore());
//        log.info("Feasible: {}", solved.isFeasible());
//
//        // 5. Log results
//        long unfilled = solved.getAssignments().stream()
//                .filter(a -> a.getAssignedEmployee() == null)
//                .count();
//
//        if (unfilled > 0) {
//            log.warn("{} role(s) could not be filled", unfilled);
//            solved.getAssignments().stream()
//                    .filter(a -> a.getAssignedEmployee() == null)
//                    .forEach(a -> log.warn("  Unfilled: Role={}", a.getRole().getName())
//                    );
//        } else {
//            log.info("All roles filled successfully!");
//            solved.getAssignments().forEach(a ->
//                    log.info("  Assigned: Role={}, Employee={} ({})",
//                            a.getRole().getName(),
//                            a.getAssignedEmployee().getNumber(),
//                            a.getAssignedEmployee().getFullname())
//            );
//        }
//
//        return solved;
//    }
//
//    /**
//     * Convert Employee entity to EmployeeFact for OptaPlanner.
//     * CRITICAL: This method extracts crew qualifications properly
//     */
//    private EmployeeFact convertToEmployeeFact(Employee employee) {
//
//        log.debug("Converting employee {} to EmployeeFact", employee.getNumber());
//
//        EmployeeFact fact = new EmployeeFact();
//        fact.setId(employee.getId());
//        fact.setNumber(employee.getNumber());
//        fact.setFullname(employee.getFullname());
//        fact.setBranchId(employee.getBranch().getId());
//        fact.setStatus(employee.getEmployeestatus().getName());
//
//        // Extract qualified roles
//        List<Integer> qualifiedRoles = new ArrayList<>();
//
//        // Check if employee is a driver
//        if (employee.getDriver() != null) {
//            log.debug("  Employee {} has driver record", employee.getNumber());
//
//            String crewStatus = employee.getDriver().getCrewstatus().getName();
//            log.debug("  Driver crew status: {}", crewStatus);
//
//            if ("Eligible".equalsIgnoreCase(crewStatus) ||
//                    "Active".equalsIgnoreCase(crewStatus)) {
//                qualifiedRoles.add(1);  // Driver role ID
//
//                // Store license category for validation
//                if (employee.getDriver().getLicensecategory() != null) {
//                    fact.setLicenseCategoryId(
//                            employee.getDriver().getLicensecategory().getId()
//                    );
//                    log.debug("  License category: {}", fact.getLicenseCategoryId());
//                }
//            }
//        } else {
//            log.debug("  Employee {} has NO driver record", employee.getNumber());
//        }
//
//        // Check if employee is a conductor
//        if (employee.getConductor() != null) {
//            log.debug("  Employee {} has conductor record", employee.getNumber());
//
//            String crewStatus = employee.getConductor().getCrewstatus().getName();
//            log.debug("  Conductor crew status: {}", crewStatus);
//
//            if ("Eligible".equalsIgnoreCase(crewStatus) ||
//                    "Active".equalsIgnoreCase(crewStatus)) {
//                qualifiedRoles.add(2);  // Conductor role ID
//
//                // Store route familiarity for optimization
//                if (employee.getConductor().getRoutefamiliaritylevel() != null) {
//                    fact.setRouteFamiliarityLevelId(
//                            employee.getConductor().getRoutefamiliaritylevel().getId()
//                    );
//                    log.debug("  Route familiarity: {}", fact.getRouteFamiliarityLevelId());
//                }
//            }
//        } else {
//            log.debug("  Employee {} has NO conductor record", employee.getNumber());
//        }
//
//        fact.setQualifiedRoles(qualifiedRoles);
//        log.info("  Final qualified roles for {}: {}", employee.getNumber(), qualifiedRoles);
//
//        // Set default values
//        fact.setPreferredShiftIds(new ArrayList<>());
////        fact.setHourlyRate(1500);  // Default rate
//
//        // Calculate experience years
//        if (employee.getDoj() != null) {
//            int years = java.time.Period.between(
//                    employee.getDoj(),
//                    java.time.LocalDate.now()
//            ).getYears();
//            fact.setExperienceYears(years);
//        } else {
//            fact.setExperienceYears(0);
//        }
//
//        fact.setUnavailableDaysOfWeek(new ArrayList<>());
////        fact.setPreferredMaxHoursPerWeek(48);
//
//        return fact;
//    }
//
//    /**
//     * Create planning entities - one for each role needed for the trip.
//     */
//    private List<TripCrewAssignmentPlanning> createPlanningAssignments(
//            Trip trip,
//            Shift derivedShift,
//            List<Role> roles) {
//
//        List<TripCrewAssignmentPlanning> assignments = new ArrayList<>();
//        int idCounter = trip.getId() * 100; // Use trip ID as base for unique IDs
//
//        for (Role role : roles) {
//            TripCrewAssignmentPlanning assignment = new TripCrewAssignmentPlanning();
//            assignment.setId(idCounter++);
//            assignment.setTrip(trip);
//            assignment.setRole(role);
//            assignment.setDerivedShift(derivedShift);
//            assignment.setAssignedEmployee(null);  // OptaPlanner fills this
//
//            assignments.add(assignment);
//        }
//
//        return assignments;
//    }
//
//    public String getConstraintViolationReport(TripCrewScheduleSolution solution) {
//        if (solution.getScore() == null) {
//            return "Solution not yet scored";
//        }
//
//        StringBuilder report = new StringBuilder();
//        report.append("Score: ").append(solution.getScore()).append("\n");
//        report.append("Feasible: ").append(solution.isFeasible()).append("\n");
//        report.append("Hard Score: ").append(solution.getScore().hardScore()).append("\n");
//        report.append("Soft Score: ").append(solution.getScore().softScore()).append("\n");
//
//        if (!solution.isFeasible()) {
//            report.append("\nHard Constraint Violations:\n");
//
//            long unassigned = solution.getAssignments().stream()
//                    .filter(a -> a.getAssignedEmployee() == null)
//                    .count();
//            if (unassigned > 0) {
//                report.append("- ").append(unassigned).append(" role(s) could not be filled\n");
//            }
//
//            report.append("\nSuggestions:\n");
//            report.append("- Check if enough qualified employees in confirmed roster\n");
//            report.append("- Verify no overlapping trip assignments\n");
//            report.append("- Check license categories match vehicle types\n");
//            report.append("- Ensure conductors have required route familiarity\n");
//        }
//
//        return report.toString();
//    }

}
