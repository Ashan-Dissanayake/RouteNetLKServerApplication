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

/**
 * Service class responsible for managing and recalculating the eligibility status
 * of crew members (drivers and conductors), including validation of medical
 * documentation and route familiarity transitions.
 */
@Service
@RequiredArgsConstructor
public class CrewEligibilityService {

    private static final CrewStatus STATUS_ACTIVE_ACTIVE = new CrewStatus(1, "Eligible");
    private static final CrewStatus STATUS_INELIGIBLE = new CrewStatus(2, "Ineligible");
    private static final CrewStatus STATUS_INACTIVE = new CrewStatus(4, "Inactive");

    private static final Map<String, List<String>> VALID_ROUTE_UPGRADES = Map.of(
            "Low", List.of("Medium"),
            "Medium", List.of("High"),
            "High", List.of()
    );

    private final DriverRepository driverRepository;
    private final ConductorRepository conductorRepository;

    /**
     * Recalculates and updates the eligibility status for all drivers
     * based on their employment status, license, and medical validity.
     */
    @Transactional
    public void recalculateDriverStatuses() {
        List<Driver> drivers = driverRepository.findAll();
        drivers.forEach(driver -> {
            CrewStatus newStatus = calculateDriverStatus(driver.getEmployee(), driver);
            updateStatusIfChanged(driver::getCrewstatus, driver::setCrewstatus, newStatus);
        });
        driverRepository.saveAll(drivers);
    }

    /**
     * Recalculates and updates the eligibility status for all conductors
     * based on their employment status and medical validity.
     */
    @Transactional
    public void recalculateConductorStatuses() {
        List<Conductor> conductors = conductorRepository.findAll();
        conductors.forEach(conductor -> {
            CrewStatus newStatus = calculateConductorStatus(conductor.getEmployee(), conductor);
            updateStatusIfChanged(conductor::getCrewstatus, conductor::setCrewstatus, newStatus);
        });
        conductorRepository.saveAll(conductors);
    }

    /**
     * Validates medical document dates against business rules.
     *
     * @param issued The date the medical document was issued.
     * @param expiry The date the medical document expires.
     * @throws BusinessRuleViolationException If dates are logically invalid or exceed the 6-month validity period.
     */
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

    /**
     * Validates if a transition between route familiarity levels is permitted.
     *
     * @param currentLevel The current familiarity level.
     * @param newLevel     The requested new familiarity level.
     * @throws IllegalArgumentException If levels are null or unknown.
     * @throws InvalidStateTransitionException If the transition is not allowed.
     */
    public void validateRouteFamiliarityTransition(String currentLevel, String newLevel) {
        if (currentLevel == null || newLevel == null) {
            throw new IllegalArgumentException("Route familiarity level cannot be null");
        }

        String trimmedCurrent = currentLevel.trim();
        String trimmedNew = newLevel.trim();

        if (trimmedCurrent.equals(trimmedNew)) {
            return;
        }

        List<String> allowedUpgrades = VALID_ROUTE_UPGRADES.get(trimmedCurrent);
        if (allowedUpgrades == null) {
            throw new IllegalArgumentException("Unknown route familiarity level: " + trimmedCurrent);
        }

        if (!allowedUpgrades.contains(trimmedNew)) {
            throw new InvalidStateTransitionException(
                    "Invalid route familiarity transition from " + trimmedCurrent + " to " + trimmedNew
            );
        }
    }

    private CrewStatus calculateDriverStatus(Employee emp, Driver driver) {
        if (isInactiveEmployee(emp)) {
            return STATUS_INACTIVE;
        }

        LocalDate today = LocalDate.now();
        boolean isLicenseValid = isDateRangeValid(driver.getDolicenseissued(), driver.getDolicenseexpired(), today);
        boolean isMedicalValid = isDateRangeValid(driver.getDomedicalissued(), driver.getDomedicalexpired(), today);

        return (isLicenseValid && isMedicalValid) ? STATUS_ACTIVE_ACTIVE : STATUS_INELIGIBLE;
    }

    private CrewStatus calculateConductorStatus(Employee emp, Conductor conductor) {
        if (isInactiveEmployee(emp)) {
            return STATUS_INACTIVE;
        }

        LocalDate today = LocalDate.now();
        boolean isMedicalValid = isDateRangeValid(conductor.getDomedicalissued(), conductor.getDomedicalexpired(), today);

        return isMedicalValid ? STATUS_ACTIVE_ACTIVE : STATUS_INELIGIBLE;
    }

    private boolean isInactiveEmployee(Employee emp) {
        return emp == null || emp.getEmployeestatus() == null ||
                !emp.getEmployeestatus().getName().equalsIgnoreCase("active");
    }

    private boolean isDateRangeValid(LocalDate issued, LocalDate expired, LocalDate targetDate) {
        if (issued == null || expired == null) {
            return false;
        }
        return !issued.isAfter(targetDate) && !expired.isBefore(targetDate);
    }

    private void updateStatusIfChanged(java.util.function.Supplier<CrewStatus> getter,
                                       java.util.function.Consumer<CrewStatus> setter,
                                       CrewStatus newStatus) {
        if (!Objects.equals(getter.get(), newStatus)) {
            setter.accept(newStatus);
        }
    }
}
