package lk.ashan.routenetlkserverapllication.dashboard.mapper;

import lk.ashan.routenetlkserverapllication.dashboard.dto.ActiveIncidentDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DashboardMapper {
    /**
     * Maps an Incident Entity directly into an ActiveIncidentDTO, flattening
     * properties across TripExecutions, Permits, Routes, and Vehicles.
     */
    @Mapping(target = "routeNumber", source = "tripexecution.trip.permite.route.number")
    @Mapping(target = "vehicleNumber", source = "tripexecution.vehicle.number")
    @Mapping(target = "issueDescription", source = "remarks")
    @Mapping(target = "status", source = "incidentstatus.name")
    ActiveIncidentDto incidentToActiveIncidentDTO(Incident incident);

    /**
     * Standard bulk array mapping utility for the live incidents stream.
     */
    List<ActiveIncidentDto> toActiveIncidentDTOList(List<Incident> incidents);
}
