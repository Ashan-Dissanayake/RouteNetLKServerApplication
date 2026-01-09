package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Seatingcapacity;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.SeatingcapacityRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidSeatingCapacityException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatingCapacityValidationStrategy implements VehicleValidationStrategy {

    private final SeatingcapacityRepository seatingcapacityRepository;

    @Override
    public void validateCreate(VehicleCreateRequestDto request) {
        Integer makeId = request.getMake().getId();
        Integer amount = request.getSeatingcapacity().getAmount();

        List<Seatingcapacity> allowedCapacities = seatingcapacityRepository.findByMakeId(makeId);

        if (allowedCapacities.isEmpty()) {
            throw new ResourceNotFoundException("No seating capacities found for the selected model.");
        }

        boolean isValid = allowedCapacities.stream()
                .anyMatch(s -> s.getAmount().equals(amount));

        if (!isValid) {
            throw new InvalidSeatingCapacityException(
                    "Selected seating capacity is not valid for the chosen model."
            );
        }
    }

    @Override
    public void validateUpdate(VehicleUpdateRequestDto request) {
        // No specific seating capacity validation on update in original code
    }
}
