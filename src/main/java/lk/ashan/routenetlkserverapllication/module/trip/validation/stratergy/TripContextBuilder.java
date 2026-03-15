package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TripContextBuilder {
    
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


    public TripUpdateContext buildForUpdate(
            Trip existingTrip,
            Integer newPermitId,
            LocalDate newServiceDate,
            LocalTime newDeparture,
            LocalTime newArrival,
            Integer newOriginTerminalId) {

        // Determine what changed
        boolean permitChanged = !existingTrip.getPermite().getId().equals(newPermitId);
        boolean timeChanged = !existingTrip.getTodepature().equals(newDeparture) ||
                !existingTrip.getToarrival().equals(newArrival);
        boolean dateChanged = !existingTrip.getDoservice().equals(newServiceDate);
        boolean terminalChanged = !existingTrip.getOriginterminal().getId().equals(newOriginTerminalId);

        // Load new permit if changed
        Permite newPermit = null;
        if (permitChanged) {
            newPermit = permitRepository.findById(newPermitId)
                    .orElseThrow(() -> new ResourceNotFoundException("New permit not found: " + newPermitId));
        }

        // Get route (from new permit if changed, else existing)
        Route route = permitChanged
                ? newPermit.getRoute()
                : existingTrip.getPermite().getRoute();

        // Fetch existing trips for validation
        List<Trip> permitRouteOriginExTrips = tripRepository
                .findByPermite_Route_IdAndOriginterminal_IdAndDoservice(
                        route.getId(),
                        newOriginTerminalId,
                        newServiceDate
                );

        // Build context
        return TripUpdateContext.builder()
                .existingTrip(existingTrip)
                .newServiceDate(newServiceDate)
                .newDeparture(newDeparture)
                .newArrival(newArrival)
                .newOriginTerminalId(newOriginTerminalId)
                .newPermitId(newPermitId)
                .permitRouteOriginExTrips(permitRouteOriginExTrips)
                .minGapMinutes(route.getMingapminutes())
                .newPermit(newPermit)
                .permitChanged(permitChanged)
                .timeChanged(timeChanged)
                .dateChanged(dateChanged)
                .terminalChanged(terminalChanged)
                .build();
    }
}
