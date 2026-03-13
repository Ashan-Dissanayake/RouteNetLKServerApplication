package lk.ashan.routenetlkserverapllication.shared.validation.driver.licensecategorylicensenumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverRequestDto;
import lk.ashan.routenetlkserverapllication.shared.validation.driver.seed.DriverValidationData;

public class LicenseCategoryLicenseNumberValidator implements ConstraintValidator<ValidLicenseCategoryLicenseNumber, DriverRequestDto> {

    private LicenseCategoryLicenseNumberValidationStrategy strategy;

    @Override
    public void initialize(ValidLicenseCategoryLicenseNumber constraintAnnotation) {
        this.strategy = new MapBasedLicenseCategoryLicenseNumberValidator(DriverValidationData.LICENSE_CATEGORY_LICENSE_NUMBER_REGEX);
    }

    @Override
    public boolean isValid(DriverRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getLicensecategory() == null || dto.getLicensenumber() ==null) return true;
        return strategy.isValid(dto.getLicensecategory().getName(),dto.getLicensenumber());

    }
}

