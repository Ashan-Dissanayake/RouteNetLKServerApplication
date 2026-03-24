package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverContextBuilder {

    public DriverValidationContext buildForCreate(DriverCreateRequestDto dto) {
        return DriverValidationContext.builder()
//                .number(dto.getNumber())
                .licenseNumber(dto.getLicensenumber())
                .licenseCategoryName(dto.getLicensecategory() != null ? dto.getLicensecategory().getName() : null)
                .licenseIssued(dto.getDolicenseissued())
                .licenseExpired(dto.getDolicenseexpired())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .build();
    }

    public DriverValidationContext buildForUpdate(DriverUpdateRequestDto dto, Driver existing) {
        return DriverValidationContext.builder()
                .id(dto.getId())
//                .number(dto.getNumber())
                .licenseNumber(dto.getLicensenumber())
                .licenseCategoryName(dto.getLicensecategory() != null ? dto.getLicensecategory().getName() : null)
                .licenseIssued(dto.getDolicenseissued())
                .licenseExpired(dto.getDolicenseexpired())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .employeeId(dto.getEmployee().getId())

                .existingNumber(
                        existing.getNumber()
                )

                .existingLicenseNumber(
                        existing.getLicensenumber()
                )

                .existingLicenseIssued(
                        existing.getDolicenseissued()
                )

                .existingEmployeeId(
                        existing.getEmployee()
                                .getId()
                )
                .build();
    }
}
