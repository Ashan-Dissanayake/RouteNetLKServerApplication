package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import org.springframework.stereotype.Component;

/**
 * Builder class for creating and updating DriverValidationContext objects.
 * This class provides methods to construct validation contexts for driver creation and updates.
 */
@Component
public class DriverContextBuilder {

    /**
     * Builds a DriverValidationContext for creating a new driver.
     *
     * @param dto the DriverCreateRequestDto containing the details of the driver to be created
     * @return a DriverValidationContext object populated with the provided driver creation details
     */
    public DriverValidationContext buildForCreate(DriverCreateRequestDto dto) {
        return DriverValidationContext.builder()
                .licenseNumber(dto.getLicensenumber())
                .licenseCategoryName(dto.getLicensecategory() != null ? dto.getLicensecategory().getName() : null)
                .licenseIssued(dto.getDolicenseissued())
                .licenseExpired(dto.getDolicenseexpired())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .employeeId(dto.getEmployee() != null ? dto.getEmployee().getId() : null)
                .build();
    }

    /**
     * Builds a DriverValidationContext for updating an existing driver.
     *
     * @param dto      the DriverUpdateRequestDto containing the updated details of the driver
     * @param existing the existing Driver entity to be updated
     * @return a DriverValidationContext object populated with the provided driver update details
     */
    public DriverValidationContext buildForUpdate(DriverUpdateRequestDto dto, Driver existing) {
        return DriverValidationContext.builder()
                .id(dto.getId())
                .licenseNumber(dto.getLicensenumber())
                .licenseCategoryName(dto.getLicensecategory() != null ? dto.getLicensecategory().getName() : null)
                .licenseIssued(dto.getDolicenseissued())
                .licenseExpired(dto.getDolicenseexpired())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .employeeId(dto.getEmployee() != null ? dto.getEmployee().getId() : null)
                .existingLicenseNumber(
                        existing.getLicensenumber()
                )
                .existingLicenseIssued(
                        existing.getDolicenseissued()
                )
                .existingEmployeeId(
                        existing.getEmployee() != null ? existing.getEmployee().getId() : null
                )
                .build();
    }
}
