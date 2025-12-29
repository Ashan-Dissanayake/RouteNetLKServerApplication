package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.crew.model.Crewstatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewEligibilityService {

    private final DriverRepository driverRepository;

    @Transactional
    public void recalculateDriverStatuses() {
        List<Driver> drivers = driverRepository.findAll();

        for (Driver driver : drivers) {
            Employee emp = driver.getEmployee();
            Crewstatus newStatus = calculateStatus(emp, driver);
            if (driver.getCrewstatus() != newStatus) {
                driver.setCrewstatus(newStatus);
            }
        }
        driverRepository.saveAll(drivers);
    }

    private Crewstatus calculateStatus(Employee emp, Driver driver) {
        if (emp.isDeleted()) {
            return new Crewstatus();
        }

        return switch (emp.getEmployeestatus().getName().toLowerCase()) {
            case "active" -> new Crewstatus(1,"Eligible");
            case "suspend", "on leave" -> new Crewstatus(2,"Ineligible");
            case "resigned" -> new Crewstatus(4,"Inactive");
            default ->new Crewstatus(4,"Inactive");
        };
    }
}
