package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
public class IncidentContext {
    private Integer tripId;
    private LocalTime reportedTime;
    private String remarks;
}
