package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.modelchassis;

import java.util.Map;

public class MapBasedModelChassisValidator implements ModelChassisValidationStrategy{

    private final Map<String, String> modelChassisMap;

    public MapBasedModelChassisValidator(Map<String, String> modelChassisMap) {
        this.modelChassisMap = modelChassisMap;
    }

    @Override
    public boolean isValid(String modelName, String chassisNumber) {
        String pattern = modelChassisMap.get(modelName);
        if (pattern == null) return true;

        String clean = chassisNumber.trim().toUpperCase();

        return clean.matches(pattern);
    }

}
