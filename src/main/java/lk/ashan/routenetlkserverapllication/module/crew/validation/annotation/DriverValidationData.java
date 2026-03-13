package lk.ashan.routenetlkserverapllication.module.crew.validation.annotation;

import java.util.Map;

public class DriverValidationData {

    public static final Map<String, String> LICENSE_CATEGORY_LICENSE_NUMBER_REGEX = Map.ofEntries(
            Map.entry("B", "^B\\d{11}$"),
            Map.entry("C1", "^C1\\d{10}$")
    );

}
