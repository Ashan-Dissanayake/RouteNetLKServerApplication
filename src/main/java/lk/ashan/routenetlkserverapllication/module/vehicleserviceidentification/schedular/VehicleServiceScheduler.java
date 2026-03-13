package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.schedular;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service.VehicleServiceIdentificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceScheduler {

    private final VehicleServiceIdentificationService vehicleServiceIdentificationService;

    /**
     * Scheduler runs every day at 2:00 AM to evaluate vehicles for maintenance.
     * Adjust cron as needed for your depot operational hours.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void runVehicleServiceEvaluation() {
        log.info("Starting Vehicle Service Evaluation Scheduler");

        try {
            vehicleServiceIdentificationService.evaluateVehicles();
            log.info("Vehicle Service Evaluation completed successfully");
        } catch (Exception ex) {
            log.error("Error during Vehicle Service Evaluation", ex);
        }
    }
}
