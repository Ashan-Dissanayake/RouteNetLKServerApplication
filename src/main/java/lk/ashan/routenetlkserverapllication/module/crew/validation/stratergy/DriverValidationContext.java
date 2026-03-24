package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class DriverValidationContext {

    private Integer id;

    // NEW VALUES

    private String number;
    private String licenseNumber;

    private String licenseCategoryName;

    private LocalDate licenseIssued;
    private LocalDate licenseExpired;

    private LocalDate medicalIssued;
    private LocalDate medicalExpired;

    private Integer employeeId;

    // EXISTING VALUES

    private String existingNumber;
    private String existingLicenseNumber;

    private LocalDate existingLicenseIssued;

    private Integer existingEmployeeId;

}
