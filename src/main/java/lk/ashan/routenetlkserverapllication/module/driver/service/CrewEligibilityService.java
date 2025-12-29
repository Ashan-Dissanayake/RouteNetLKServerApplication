package lk.ashan.routenetlkserverapllication.module.driver.service;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.driver.model.Crewstatus;
import lk.ashan.routenetlkserverapllication.module.driver.model.Driver;
import lk.ashan.routenetlkserverapllication.module.driver.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
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
