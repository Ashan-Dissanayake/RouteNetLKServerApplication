package lk.ashan.routenetlkserverapllication.module.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OverrideSuggestionResponse {
    private Integer tripId;
    private Integer suggestedVehicleId;
}
