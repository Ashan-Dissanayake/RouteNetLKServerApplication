package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ConductorValidator {

    private final ConductorRepository conductorRepository;

    public void validateCreate(ConductorCreateRequestDto dto) {

        if (!dto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new BusinessRuleViolationException(
                    "New conductor must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName().equalsIgnoreCase("Low")) {
            throw new BusinessRuleViolationException(
                    "New conductor route familiarity must have 'LOW'");
        }

        if (conductorRepository.existsByEmployeeId(
                dto.getEmployee().getId())) {

            throw new ResourceExistsException(
                    "A conductor profile already exists for this employee");
        }

        validateMedical(dto.getDomedicalissued(), dto.getDomedicalexpired());
    }

    public void validateUpdate(Conductor existing, ConductorUpdateRequestDto dto) {

        if (!existing.getEmployee().getId().equals(
                dto.getEmployee().getId())) {

            if (conductorRepository.existsByEmployeeIdAndIdNot(
                    dto.getEmployee().getId(),
                    dto.getId())) {

                throw new ResourceExistsException(
                        "A conductor profile already exists for this employee");
            }
        }

        validateMedical(dto.getDomedicalissued(), dto.getDomedicalexpired());
    }

    private void validateMedical(LocalDate issued, LocalDate expiry) {

        if (!expiry.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    "Medical expiry must be in the future");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);

        if (months > 6) {
            throw new BusinessRuleViolationException(
                    "Medical validity cannot exceed 6 months");
        }
    }
}