package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class DriverValidationContext {

    private Integer id;

    private String number;
    private String licenseNumber;

    private String licenseCategoryName;

    private LocalDate licenseIssued;
    private LocalDate licenseExpired;

    private LocalDate medicalIssued;
    private LocalDate medicalExpired;

}
