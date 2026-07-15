package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import org.springframework.stereotype.Component;

/**
 * Builder class for creating instances of {@link ConductorValidationContext}.
 * This class provides methods to build validation contexts for creating and updating conductors.
 */
@Component
public class ConductorContextBuilder {

    /**
     * Builds a {@link ConductorValidationContext} for creating a conductor.
     *
     * @param dto the data transfer object containing the details for creating a conductor
     * @return a {@link ConductorValidationContext} populated with the provided creation details
     */
    public ConductorValidationContext buildForCreate(ConductorCreateRequestDto dto) {
        return ConductorValidationContext.builder()
                .employeeId(dto.getEmployee().getId())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .build();
    }

    /**
     * Builds a {@link ConductorValidationContext} for updating a conductor.
     *
     * @param dto the data transfer object containing the details for updating a conductor
     * @return a {@link ConductorValidationContext} populated with the provided update details
     */
    public ConductorValidationContext buildForUpdate(ConductorUpdateRequestDto dto) {
        return ConductorValidationContext.builder()
                .id(dto.getId())
                .employeeId(dto.getEmployee().getId())
                .medicalIssued(dto.getDomedicalissued())
                .medicalExpired(dto.getDomedicalexpired())
                .build();
    }

}
