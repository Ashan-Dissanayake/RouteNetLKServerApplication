package lk.ashan.routenetlkserverapllication.module.crew.validation.annotation;

import java.util.Map;

/**
 * This class contains validation data for driver license categories
 * and their corresponding license number regex patterns.
 */
public class DriverValidationData {

    /**
     * A map that associates driver license categories with their respective
     * regex patterns for validating license numbers.
     * <ul>
     *     <li>Key: License category (e.g., "D", "D1")</li>
     *     <li>Value: Regex pattern for validating license numbers</li>
     * </ul>
     */
    public static final Map<String, String> LICENSE_CATEGORY_LICENSE_NUMBER_REGEX = Map.ofEntries(
            Map.entry("D", "^D\\d{11}$"),
            Map.entry("D1", "^D1\\d{10}$")
    );

}
