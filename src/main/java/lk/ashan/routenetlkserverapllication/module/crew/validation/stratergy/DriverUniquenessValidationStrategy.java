package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverUniquenessValidationStrategy implements DriverValidationStrategy {

    private final DriverRepository driverRepository;

    @Override
    public void validateCreate(DriverValidationContext context) {

        if (driverRepository.existsByLicensenumber(context.getLicenseNumber())) {
            throw new ResourceExistsException("License number already exists");
        }

        if (driverRepository.existsByNumber(context.getNumber())) {
            throw new ResourceExistsException("Driver number already exists");
        }

    }

    @Override
    public void validateUpdate(DriverValidationContext context) {

        if (driverRepository.existsByLicensenumberAndIdNot(context.getLicenseNumber(), context.getId())) {
            throw new ResourceExistsException("License number already exists");
        }

        if (driverRepository.existsByNumberAndIdNot(context.getNumber(), context.getId())) {
            throw new ResourceExistsException("Driver number already exists");
        }

    }
}
