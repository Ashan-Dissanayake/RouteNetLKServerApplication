package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.modelengine;

import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.modelchassis.ModelChassisValidationStrategy;

import java.util.Map;

public class MapBasedModelEngineValidator implements ModelChassisValidationStrategy {

    private final Map<String, String> modelEngineMap;

    public MapBasedModelEngineValidator(Map<String, String> modelEngineMap) {
        this.modelEngineMap = modelEngineMap;
    }

    @Override
    public boolean isValid(String modelName, String enginenumber) {
        String allowedEngines = modelEngineMap.get(modelName);
        if (allowedEngines == null) return true;
        return enginenumber.matches(allowedEngines);
    }
}
