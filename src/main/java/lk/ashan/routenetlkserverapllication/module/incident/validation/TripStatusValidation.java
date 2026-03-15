package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripStatusValidation implements IncidentCreationStrategy {

    private final TripRepository tripRepository;

    @Override
    public void validate(IncidentContext context) {

        Tripstatus tripstatus = tripRepository.findById(context.getTripId())
                .orElseThrow(() -> new BusinessRuleViolationException("Trip not found"))
                .getTripstatus();

        String status = tripstatus.getName().toUpperCase();
        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            throw new BusinessRuleViolationException("Cannot create incident for completed or cancelled trip");
        }
    }
}
