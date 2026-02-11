package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.springframework.stereotype.Component;

@Component
public class TripNumberUniquenessValidationStrategy implements TripValidationStrategy {

    @Override
    public void validate(TripValidationContext context) {
        for (Trip existing : context.getPermitDoServiceExTrips()) {
           if (existing.getNotrip() == context.getTripNo()){
               throw new  ResourceExistsException("This trip number already exists");
           }
        }
    }
}
