package lk.ashan.routenetlkserverapllication.module.crew.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;

/**
 * Validator for ensuring the validity of the license category and license number
 * in a `DriverRequestDto` object.
 */
public class LicenseCategoryLicenseNumberValidator implements ConstraintValidator<ValidLicenseCategoryLicenseNumber, DriverCreateRequestDto> {

    private LicenseCategoryLicenseNumberValidationStrategy strategy;

    /**
     * Initializes the validator with a specific validation strategy.
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(ValidLicenseCategoryLicenseNumber constraintAnnotation) {
        this.strategy = new MapBasedLicenseCategoryLicenseNumberValidator(DriverValidationData.LICENSE_CATEGORY_LICENSE_NUMBER_REGEX);
    }

    /**
     * Validates the license category and license number in the provided `DriverRequestDto`.
     *
     * @param dto the `DriverRequestDto` containing the license category and license number to validate
     * @param context the context in which the constraint is evaluated
     * @return `true` if the license category or license number is null, or if the validation strategy deems them valid;
     *         `false` otherwise
     */
    @Override
    public boolean isValid(DriverCreateRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getLicensecategory() == null || dto.getLicensenumber() == null) return true;
        return strategy.isValid(dto.getLicensecategory().getName(), dto.getLicensenumber());
    }
}
