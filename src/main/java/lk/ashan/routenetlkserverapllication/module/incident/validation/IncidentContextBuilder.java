package lk.ashan.routenetlkserverapllication.module.incident.validation;


import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Builder class for creating an IncidentContext object.
 */
@Component
public class IncidentContextBuilder {

    /**
     * Builds an IncidentContext object for creating a new incident.
     *
     * @param dto the data transfer object containing the details for the incident creation
     * @return an IncidentContext object populated with the provided details
     * @throws NullPointerException if any required fields in the dto are null
     */
    public IncidentContext buildForCreate(IncidentCreateRequestDto dto) {
        return IncidentContext.builder()
                .tripId(dto.getTripexecution().getId())
                .reportedTime(dto.getToreported())
                .remarks(dto.getRemarks())
                .build();
    }
}
