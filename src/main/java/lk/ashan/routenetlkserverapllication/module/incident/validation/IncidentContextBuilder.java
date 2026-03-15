package lk.ashan.routenetlkserverapllication.module.incident.validation;


import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class IncidentContextBuilder {
    public IncidentContext buildForCreate(IncidentCreateRequestDto dto) {
        return IncidentContext.builder()
                .tripId(dto.getTrip().getId())
                .reportedTime(dto.getToreported())
                .remarks(dto.getRemarks())
                .build();
    }
}
