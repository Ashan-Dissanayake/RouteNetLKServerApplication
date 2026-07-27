package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripContextBuilder {

    public TripValidationContext buildForCreate(TripCreateRequestDto dto) {
        return TripValidationContext.builder()
                .permitId(dto.getPermite().getId())
                .departure(dto.getTodepature())
                .arrival(dto.getToarrival())
                .routeId(dto.getPermite().getRoute().getId())
                .originTerminalId(dto.getOriginterminal().getId())
                .triptypeId(dto.getTriptype().getId())
                .build();
    }
}
