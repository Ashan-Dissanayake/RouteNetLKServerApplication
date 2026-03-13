package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
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
            CrewStatus newStatus = calculateDriverStatus(emp,driver);
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
            CrewStatus newStatus = calculateConductorStatus(emp,conductor);
            if (!Objects.equals(conductor.getCrewstatus(), newStatus)) {
                conductor.setCrewstatus(newStatus);
            }
        }
        conductorRepository.saveAll(conductors);
    }

    private CrewStatus calculateDriverStatus(Employee emp, Driver driver) {
        LocalDate today = LocalDate.now();

        if (!emp.getEmployeestatus().getName().equalsIgnoreCase("active")) {
            return new CrewStatus(4,"Inactive");
        }

        // Check license validity
        boolean licenseInvalid = driver.getDolicenseissued().isAfter(today)
                || driver.getDolicenseexpired().isBefore(today);

        // Check medical validity
        boolean medicalInvalid = driver.getDomedicalissued().isAfter(today)
                || driver.getDomedicalexpired().isBefore(today);

        // If either is invalid, driver is ineligible
        if (licenseInvalid || medicalInvalid) {
            return new CrewStatus(2, "Ineligible");
        }

        return new CrewStatus(1,"Eligible");
    }

    private CrewStatus calculateConductorStatus(Employee emp, Conductor conductor) {
        LocalDate today = LocalDate.now();

        if (!emp.getEmployeestatus().getName().equalsIgnoreCase("active")) {
            return new CrewStatus(4,"Inactive");
        }

        // Check license expiry
        if (conductor.getDomedicalexpired().isBefore(today)
                || conductor.getDomedicalissued().isAfter(today)) {
            return new CrewStatus(2,"Ineligible");
        }

        return new CrewStatus(1,"Eligible");
    }

    public void validateMedicalDates(LocalDate issued, LocalDate expiry) {
        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("Medical issued date cannot be in the future");
        }
        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleViolationException("Medical expiry must be after issued date");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);
        if (months > 6) {
            throw new BusinessRuleViolationException("Medical validity cannot exceed 6 months");
        }
    }

    public void validateRouteFamiliarityTransition(String currentLevel, String newLevel) {

        if (currentLevel == null || newLevel == null) {
            throw new IllegalArgumentException("Route familiarity level cannot be null");
        }

        currentLevel = currentLevel.trim();
        newLevel = newLevel.trim();

        if (currentLevel.equals(newLevel)) return;

        List<String> allowedUpgrades = VALID_ROUTE_UPGRADES.get(currentLevel);
        if (allowedUpgrades == null) {
            throw new IllegalArgumentException("Unknown route familiarity level: " + currentLevel);
        }

        if (!allowedUpgrades.contains(newLevel)) {
            throw new InvalidStateTransitionException(
                    "Invalid route familiarity transition from " + currentLevel + " to " + newLevel
            );
        }
    }

    private static final Map<String, List<String>> VALID_ROUTE_UPGRADES = Map.of(
            "Low", List.of("Medium"),
            "Medium", List.of("High"),
            "High", List.of()
    );

}
