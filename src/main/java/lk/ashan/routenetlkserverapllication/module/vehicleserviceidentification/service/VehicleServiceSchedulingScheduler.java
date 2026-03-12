package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceSchedulingScheduler {

    private final VehicleServiceSchedulerService vehicleServiceSchedulerService;

    /**
     * Runs daily at 2:00 AM to schedule identified services
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void runVehicleServiceScheduling() {
        log.info("Starting Vehicle Service Scheduling Scheduler");

        try {
            vehicleServiceSchedulerService.scheduleServices();
            log.info("Vehicle Service Scheduling completed successfully");
        } catch (Exception ex) {
            log.error("Error during Vehicle Service Scheduling", ex);
        }
    }
}
