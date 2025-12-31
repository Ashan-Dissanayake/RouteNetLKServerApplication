package lk.ashan.routenetlkserverapllication.module.crew.schedule;

import lk.ashan.routenetlkserverapllication.module.crew.service.CrewEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CrewEligibilityScheduler {

    private final CrewEligibilityService crewEligibilityService;

    // Run every day at 1:00 AM
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateDriverStatuses() {
        crewEligibilityService.recalculateDriverStatuses();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateConductorStatuses() {
        crewEligibilityService.recalculateConductorStatuses();
    }
}

