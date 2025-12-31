package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.crew.model.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.Crewstatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CrewEligibilityService {

    private final DriverRepository driverRepository;
    private final ConductorRepository conductorRepository;

    @Transactional
    public void recalculateDriverStatuses() {
        List<Driver> drivers = driverRepository.findAll();

        for (Driver driver : drivers) {
            Employee emp = driver.getEmployee();
            Crewstatus newStatus = calculateDriverStatus(emp,driver);
            if (!Objects.equals(driver.getCrewstatus(), newStatus)) {
                driver.setCrewstatus(newStatus);
            }
        }
        driverRepository.saveAll(drivers);
    }
    
    @Transactional
    public void recalculateConductorStatuses() {
        List<Conductor> conductors = conductorRepository.findAll();

        for (Conductor conductor : conductors) {
            Employee emp = conductor.getEmployee();
            Crewstatus newStatus = calculateConductorStatus(emp,conductor);
            if (!Objects.equals(conductor.getCrewstatus(), newStatus)) {
                conductor.setCrewstatus(newStatus);
            }
        }
        conductorRepository.saveAll(conductors);
    }

    private Crewstatus calculateDriverStatus(Employee emp, Driver driver) {
        LocalDate today = LocalDate.now();

        if (!emp.getEmployeestatus().getName().equalsIgnoreCase("active")) {
            return new Crewstatus(4,"Inactive");
        }

        // Check license validity
        boolean licenseInvalid = driver.getDolicenseissued().isAfter(today)
                || driver.getDolicenseexpired().isBefore(today);

        // Check medical validity
        boolean medicalInvalid = driver.getDomedicalissued().isAfter(today)
                || driver.getDomedicalexpired().isBefore(today);

        // If either is invalid, driver is ineligible
        if (licenseInvalid || medicalInvalid) {
            return new Crewstatus(2, "Ineligible");
        }

        return new Crewstatus(1,"Eligible");
    }


    private Crewstatus calculateConductorStatus(Employee emp, Conductor conductor) {
        LocalDate today = LocalDate.now();

        if (!emp.getEmployeestatus().getName().equalsIgnoreCase("active")) {
            return new Crewstatus(4,"Inactive");
        }

        // Check license expiry
        if (conductor.getDomedicalexpired().isBefore(today)
                || conductor.getDomedicalissued().isAfter(today)) {
            return new Crewstatus(2,"Ineligible");
        }

        return new Crewstatus(1,"Eligible");
    }

}
