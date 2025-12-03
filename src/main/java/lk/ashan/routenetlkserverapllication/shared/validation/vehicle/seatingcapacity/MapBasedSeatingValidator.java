package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seatingcapacity;

import java.util.List;
import java.util.Map;

public class MapBasedSeatingValidator implements SeatingValidationStrategy {
    private final Map<String, List<Integer>> modelSeatingMap;

    public MapBasedSeatingValidator(Map<String, List<Integer>> modelSeatingMap) {
        this.modelSeatingMap = modelSeatingMap;
    }

    @Override
    public boolean isValid(String modelName, Integer amount) {
        List<Integer> allowedSeats = modelSeatingMap.get(modelName);
        if (allowedSeats == null) return true; // skip unknown models
        return allowedSeats.contains(amount);
    }
}
