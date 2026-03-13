package lk.ashan.routenetlkserverapllication.module.crew.validation;

import java.util.Map;


public class MapBasedLicenseCategoryLicenseNumberValidator implements LicenseCategoryLicenseNumberValidationStrategy {

    private final Map<String, String> licenseCategoryLicenseNumber;

    public MapBasedLicenseCategoryLicenseNumberValidator(Map<String, String> licenseCategoryLicensesNumber) {
        this.licenseCategoryLicenseNumber = licenseCategoryLicensesNumber;
    }

    @Override
    public boolean isValid(String licenseCategory, String licenseNumber) {
        String pattern = licenseCategoryLicenseNumber.get(licenseCategory);
        if (pattern == null) return true;

        String clean = licenseNumber.trim().toUpperCase();

        return clean.matches(pattern);
    }

}
