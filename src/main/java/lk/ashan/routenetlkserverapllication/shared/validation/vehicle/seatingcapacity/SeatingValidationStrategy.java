package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seatingcapacity;

public interface SeatingValidationStrategy {
    boolean isValid(String modelName, Integer amount);
}
