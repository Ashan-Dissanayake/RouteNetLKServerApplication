package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ConductorMedicalValidationStrategy implements ConductorValidationStrategy {

    @Override
    public void validateCreate(ConductorCreateRequestDto request) {
        validateMedicalDates(request.getDomedicalissued(), request.getDomedicalexpired());
    }

    @Override
    public void validateUpdate(ConductorUpdateRequestDto request) {
        validateMedicalDates(request.getDomedicalissued(), request.getDomedicalexpired());
    }

    private void validateMedicalDates(LocalDate issued, LocalDate expiry) {
        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("Medical issued date cannot be in the future");
        }
        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleViolationException("Medical expiry must be after issued date");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);
        if (months > 6) {
            throw new BusinessRuleViolationException("Medical validity cannot exceed 6 months");
        }
    }
}
