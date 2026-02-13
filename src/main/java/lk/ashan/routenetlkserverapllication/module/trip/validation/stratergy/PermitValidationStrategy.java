package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;


import jakarta.validation.ValidationException;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PermitValidationStrategy implements TripValidationStrategy {
    
    @Override
    public void validate(TripValidationContext context) {
        Permite permit = context.getPermit();
        LocalDate serviceDate = context.getServiceDate();
        
        // Check if permit exists
        if (permit == null) {
            throw new ValidationException("Permit is required");
        }
        
        // Check permit status - must be ACTIVE
        String permitStatus = permit.getPermitestatus().getName();
        if (!"ACTIVE".equalsIgnoreCase(permitStatus)) {
            throw new ValidationException(
                "Permit is not active. Current status: " + permitStatus
            );
        }
        
        // Check if permit is valid on service date
        LocalDate issueDate = permit.getDoissued();
        LocalDate expiryDate = permit.getDoexpired();
        
        if (issueDate != null && serviceDate.isBefore(issueDate)) {
            throw new ValidationException(
                "Service date is before permit issue date"
            );
        }
        
        if (expiryDate != null && serviceDate.isAfter(expiryDate)) {
            throw new ValidationException(
                "Permit has expired. Expiry date: " + expiryDate
            );
        }
    }
}
