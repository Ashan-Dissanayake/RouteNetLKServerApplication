package lk.ashan.routenetlkserverapllication.module.trip.validation.context;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripCreateContextBuilder {
    
    private final TripRepository tripRepository;
    private final PermitRepository permitRepository;
    private final RouteRepository routeRepository;

    public TripCreateContext buildForCreation(TripCreateRequestDto createRequestDto) {
        
        // Fetch permit
        Permite permit = permitRepository.findById(createRequestDto.getPermite().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Permit not found: " + createRequestDto.getPermite().getId()));
        
        // Fetch route
        Route route = routeRepository.findById(permit.getRoute().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Route not found for permit"));
        
        // Fetch existing trips for route/origin/date validation
        List<Trip> permitRouteOriginExTrips = tripRepository
            .findByPermite_Route_IdAndOriginterminal_IdAndDoservice(
                route.getId(),
                createRequestDto.getOriginterminal().getId(),
                createRequestDto.getDoservice()
            );
        
        // Fetch existing trips for permit/date validation
        List<Trip> permitDoServiceExTrips = tripRepository
            .findByPermite_IdAndDoservice(permit.getId(), createRequestDto.getDoservice());
        
        // Build context
        return TripCreateContext.builder()
            .permit(permit)
            .serviceDate(createRequestDto.getDoservice())
            .requestedDeparture(createRequestDto.getTodepature())
            .minGapMinutes(route.getMingapminutes())
            .permitRouteOriginExTrips(permitRouteOriginExTrips)
            .permitDoServiceExTrips(permitDoServiceExTrips)
            .build();
    }
}
