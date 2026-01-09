package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverUniquenessValidationStrategy implements DriverValidationStrategy {

    private final DriverRepository driverRepository;

    @Override
    public void validateCreate(DriverCreateRequestDto request) {
        if (driverRepository.existsByLicensenumber(request.getLicensenumber())) {
            throw new ResourceExistsException("License number already exists");
        }

        if (driverRepository.existsByNumber(request.getNumber())) {
            throw new ResourceExistsException("Driver number already exists");
        }
    }

    @Override
    public void validateUpdate(DriverUpdateRequestDto request) {
        if (driverRepository.existsByLicensenumberAndIdNot(request.getLicensenumber(), request.getId())) {
            throw new ResourceExistsException("License number already exists");
        }

        if (driverRepository.existsByNumberAndIdNot(request.getNumber(), request.getId())) {
            throw new ResourceExistsException("Driver number already exists");
        }
    }
}
