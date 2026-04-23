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

    private final TripRepository tripRepository;

    public TripValidationContext buildForCreate(TripCreateRequestDto dto) {
        List<Trip> existing = tripRepository.findByOriginterminal_Id(dto.getOriginterminal().getId());

        return TripValidationContext.builder()
                .permitId(dto.getPermite().getId())
                .departure(dto.getTodepature())
                .arrival(dto.getToarrival())
                .originTerminalId(dto.getOriginterminal().getId())
                .existingTripsAtTerminal(existing)
                .triptypeId(dto.getTriptype().getId())
                .minGapMinutes(10) // Could be fetched from a config table
                .build();
    }
}
