package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.crew.model.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Mapper to convert JPA Employee entities to OptaPlanner EmployeePlanning entities.
 */
@Component
public class EmployeePlanningMapper {

    /**
     * Convert Employee entity to EmployeePlanning for optimization
     */
    public EmployeePlanning toEmployeePlanning(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeePlanning planning = new EmployeePlanning();
        planning.setId(employee.getId());
        planning.setNumber(employee.getNumber());
        planning.setName(employee.getCallingname() != null ?
                employee.getCallingname() : employee.getFullname());
        planning.setBranchId(employee.getBranch() != null ?
                employee.getBranch().getId() : null);
        planning.setDesignationId(employee.getDesignation() != null ?
                employee.getDesignation().getId() : null);

        // Determine role based on designation
        Integer designationId = planning.getDesignationId();
        planning.setDriver(designationId != null && designationId == 1);
        planning.setConductor(designationId != null && designationId == 2);

        // Map driver-specific fields
        if (planning.isDriver()) {
            Driver driver = employee.getDriver();
            if (driver != null) {
                planning.setLicenseCategoryId(driver.getLicensecategory() != null ?
                        driver.getLicensecategory().getId() : null);
                planning.setLicenseExpiry(driver.getDolicenseexpired());
                planning.setMedicalExpiry(driver.getDomedicalexpired());
                planning.setRouteFamiliarityLevelId(driver.getRoutefamiliaritylevel() != null ?
                        driver.getRoutefamiliaritylevel().getId() : null);

                // Set crew status from driver
                planning.setCrewStatusId(driver.getCrewstatus() != null ?
                        driver.getCrewstatus().getId() : null);

                // Map allowed bus types (if available)
                if (driver.getLicensecategory() != null &&
                        driver.getLicensecategory().getLicensecategoryallowedbustypes() != null) {
                    Set<Integer> allowedBusTypes = new HashSet<>();
                    driver.getLicensecategory().getLicensecategoryallowedbustypes()
                            .forEach(lc -> {
                                if (lc.getBustype() != null) {
                                    allowedBusTypes.add(lc.getBustype().getId());
                                }
                            });
                    planning.setAllowedBusTypeIds(allowedBusTypes);
                }
            }
        }

        // Map conductor-specific fields
        if (planning.isConductor()) {
            Conductor conductor = employee.getConductor();
            if (conductor != null) {
                planning.setMedicalExpiry(conductor.getDomedicalexpired());
                planning.setRouteFamiliarityLevelId(conductor.getRoutefamiliaritylevel() != null ?
                        conductor.getRoutefamiliaritylevel().getId() : null);

                // Set crew status from conductor
                planning.setCrewStatusId(conductor.getCrewstatus() != null ?
                        conductor.getCrewstatus().getId() : null);
            }
        }

        return planning;
    }

    /**
     * Batch convert multiple employees
     */
    public java.util.List<EmployeePlanning> toEmployeePlanningList(
            java.util.List<Employee> employees
    ) {
        if (employees == null) {
            return new java.util.ArrayList<>();
        }

        return employees.stream()
                .map(this::toEmployeePlanning)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toList());
    }
}

