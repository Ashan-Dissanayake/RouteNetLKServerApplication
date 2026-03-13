package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incidenttype;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class IncidentCreationContext {
    private Trip trip;
    private Incidenttype type;
    private LocalTime reportedTime;
    private String remarks;
}
