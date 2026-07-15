package lk.ashan.routenetlkserverapllication.module.crew.validation.annotation;

import java.util.Map;


/**
 * A validator that checks if a given license number matches the pattern
 * associated with a specific license category. The patterns are provided
 * as a map during initialization.
 */
public class MapBasedLicenseCategoryLicenseNumberValidator implements LicenseCategoryLicenseNumberValidationStrategy {

    private final Map<String, String> licenseCategoryLicenseNumber;

    /**
     * Constructs a new validator with the specified map of license categories
     * and their corresponding license number patterns.
     *
     * @param licenseCategoryLicensesNumber a map where the key is the license category
     *                                      and the value is the regex pattern for the license number
     */
    public MapBasedLicenseCategoryLicenseNumberValidator(Map<String, String> licenseCategoryLicensesNumber) {
        this.licenseCategoryLicenseNumber = licenseCategoryLicensesNumber;
    }

    /**
     * Validates whether the given license number matches the pattern associated
     * with the specified license category.
     *
     * @param licenseCategory the category of the license
     * @param licenseNumber   the license number to validate
     * @return {@code true} if the license number matches the pattern or if no pattern
     *         is defined for the category; {@code false} otherwise
     */
    @Override
    public boolean isValid(String licenseCategory, String licenseNumber) {
        String pattern = licenseCategoryLicenseNumber.get(licenseCategory);
        if (pattern == null) return true;

        String clean = licenseNumber.trim().toUpperCase();

        return clean.matches(pattern);
    }

}
