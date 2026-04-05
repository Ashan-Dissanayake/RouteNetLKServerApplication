package lk.ashan.routenetlkserverapllication.module.partreqest.validation;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DuplicatePartInOpenRequestsValidation implements PartRequestValidationStrategy {

    private final PartRequestRepository repository;

    @Override
    public void validate(PartRequestValidationContext context) {
        for (PartRequestItemDto item : context.getItems()) {
            boolean exists = repository.existsByBranchAndPartAndStatusInAndDoRequested(
                    context.getBranchId(),
                    item.getPart().getId(),
                    List.of("Pending", "Approved"),
                    context.getRequestedate()
            );

            if (exists) {
                throw new BusinessRuleViolationException(
                        "Part " + item.getPart().getName() + " has already been requested and is still pending."
                );
            }
        }
    }
}
