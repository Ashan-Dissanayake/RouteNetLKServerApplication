package lk.ashan.routenetlkserverapllication.module.crew.schedule;

import lk.ashan.routenetlkserverapllication.module.crew.service.CrewEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Scheduler class for managing crew eligibility updates.
 * This class is responsible for scheduling tasks to update the statuses
 * of drivers and conductors on a daily basis.
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CrewEligibilityScheduler {

    private final CrewEligibilityService crewEligibilityService;

    /**
     * Updates the statuses of drivers.
     * This method is scheduled to run every day at 1:00 AM.
     *
     * @throws RuntimeException if an error occurs during the status update process
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateDriverStatuses() {
        crewEligibilityService.recalculateDriverStatuses();
    }

    /**
     * Updates the statuses of conductors.
     * This method is scheduled to run every day at 1:00 AM.
     *
     * @throws RuntimeException if an error occurs during the status update process
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateConductorStatuses() {
        crewEligibilityService.recalculateConductorStatuses();
    }
}
