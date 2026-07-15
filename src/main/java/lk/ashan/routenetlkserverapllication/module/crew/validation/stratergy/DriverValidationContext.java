package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

/**
 * Represents the validation context for a driver, containing both new and existing values
 * related to the driver's details, license, and medical information.
 */
@Getter
@Builder
public class DriverValidationContext {

    /**
     * The unique identifier for the driver validation context.
     */
    private Integer id;

    // NEW VALUES

    /**
     * The driver's number.
     */
    private String number;

    /**
     * The driver's license number.
     */
    private String licenseNumber;

    /**
     * The name of the driver's license category.
     */
    private String licenseCategoryName;

    /**
     * The date the driver's license was issued.
     */
    private LocalDate licenseIssued;

    /**
     * The date the driver's license expires.
     */
    private LocalDate licenseExpired;

    /**
     * The date the driver's medical certificate was issued.
     */
    private LocalDate medicalIssued;

    /**
     * The date the driver's medical certificate expires.
     */
    private LocalDate medicalExpired;

    /**
     * The unique identifier for the employee associated with the driver.
     */
    private Integer employeeId;

    // EXISTING VALUES

    /**
     * The existing driver's number.
     */
    private String existingNumber;

    /**
     * The existing driver's license number.
     */
    private String existingLicenseNumber;

    /**
     * The date the existing driver's license was issued.
     */
    private LocalDate existingLicenseIssued;

    /**
     * The unique identifier for the existing employee associated with the driver.
     */
    private Integer existingEmployeeId;

}
