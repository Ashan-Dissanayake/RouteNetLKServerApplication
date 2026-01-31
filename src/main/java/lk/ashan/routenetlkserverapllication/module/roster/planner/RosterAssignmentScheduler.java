package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RosterAssignmentScheduler {

    private final RosterAssignmentService rosterAssignmentService;
    private final BranchRepository branchRepository;

    /**
     * Execute daily roster assignment at 1:00 AM.
     * Processes all active branches.
     * Cron expression: "0 0 1 * * ?" = Every day at 1:00 AM
     */
    @Scheduled(cron = "${roster.assignment.cron:0 0 1 * * ?}")
    public void assignDailyRosters() {
        LocalDate today = LocalDate.now();

        // List of branches to process (can be loaded from database)
        List<Integer> branchIds = getBranchesToProcess();

        int successCount = 0;
        int failureCount = 0;

        for (Integer branchId : branchIds) {
            try {
                log.info("Processing branch: {}", branchId);

                RosterAssignmentSolution solution =
                        rosterAssignmentService.executeAssignment(branchId, today);

                if (solution != null) {
                    successCount++;
                    log.info("Branch {} processed successfully. Score: {}",
                            branchId, solution.getScore());
                } else {
                    log.info("Branch {} had no rosters to process", branchId);
                }

            } catch (Exception e) {
                failureCount++;
                log.error("Failed to process branch {}", branchId, e);
                // Continue processing other branches
            }
        }

        log.info("=== Daily Roster Assignment Job Completed ===");
        log.info("Success: {}, Failures: {}", successCount, failureCount);
    }

    /**
     * Manual trigger for roster assignment (for testing or manual runs).
     * Can be called via REST endpoint or admin interface.
     */
    public void manualAssignment(Integer branchId, LocalDate date) {
        log.info("Manual roster assignment triggered for branch {} on {}", branchId, date);

        try {
            RosterAssignmentSolution solution = rosterAssignmentService.executeAssignment(branchId, date);

            if (solution != null) {
                log.info("Manual assignment completed. Score: {}", solution.getScore());

            } else {
                log.info("No rosters to process for the given criteria");
            }

        } catch (Exception e) {
            log.error("Manual assignment failed", e);
            throw new RuntimeException("Manual assignment failed: " + e.getMessage(), e);
        }
    }


    /**
     * Get list of branches to process.
     * Can be configured via properties or loaded from database.
     */
    private List<Integer> getBranchesToProcess() {
        // Alternative: Load from database
         return branchRepository.findByDeletedFalseAndBranchstatus_Name("Active")
             .stream()
             .map(Branch::getId)
             .collect(Collectors.toList());
    }
}
