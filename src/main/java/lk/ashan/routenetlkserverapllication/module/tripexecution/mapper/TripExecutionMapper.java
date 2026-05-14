package lk.ashan.routenetlkserverapllication.module.tripexecution.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.planner.CrewFact;
import lk.ashan.routenetlkserverapllication.module.tripexecution.planner.RouteFact;
import lk.ashan.routenetlkserverapllication.module.tripexecution.planner.TripExecutionPlanning;
import lk.ashan.routenetlkserverapllication.module.tripexecution.planner.VehicleFact;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = BranchMapper.class)
public interface TripExecutionMapper {

    @Mapping(source = "tripexecutionstatus.name", target = "status")
    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "trip.triptype.name", target = "tripType")
    @Mapping(source = "trip.todepature", target = "plannedDeparture")
    @Mapping(source = "trip.toarrival", target = "plannedArrival")
    @Mapping(target = "routeName", expression = "java(mapRouteName(entity))")
    @Mapping(source = "vehicle.number", target = "vehicleNumber")
    @Mapping(source = "driver.employee.callingname", target = "driverName")
    @Mapping(source = "conductor.employee.callingname", target = "conductorName")
    @Mapping(source = "startodometer", target = "startodometer")
    @Mapping(source = "endodometer", target = "endodometer")
    @Mapping(source = "passengercount", target = "passengercount")
    TripExecutionDetailsResponseDto toDto(TripExecution entity);
    List<TripExecutionDetailsResponseDto> toDtoList(List<TripExecution> entities);

    @Mapping(target = "name", expression = "java(mapRouteName(entity))")
    TripExecutionSummaryDto toSummaryDto(TripExecution entity);
    List<TripExecutionSummaryDto> toSummaryDtoList(List<TripExecution> entities);

    default String mapRouteName(TripExecution entity) {
        if (entity == null || entity.getTrip() == null || entity.getTrip().getPermite() == null) {
            return null;
        }
        var route = entity.getTrip().getPermite().getRoute();
        String number = (route.getNumber() != null) ? route.getNumber() : "";
        String origin = (route.getOrigin() != null) ? route.getOrigin() : "";
        String destination = (route.getDestination() != null) ? route.getDestination() : "";
        return String.format("%s %s - %s", number, origin, destination);
    }

    @Mapping(target = "departureTime", source = "trip.todepature")
    @Mapping(target = "arrivalTime", source = "trip.toarrival")
    @Mapping(target = "route", source = "trip.permite.route")
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "conductor", ignore = true)
    TripExecutionPlanning toPlanning(TripExecution entity);

    @Mapping(target = "requiredFamiliarityLevel", source = "requiredroutefamiliaritylevel.id")
    @Mapping(target = "distanceKm", source = "distancekm")
    RouteFact toRouteFact(Route route);

    @Mapping(target = "familiarityLevel", source = "routefamiliaritylevel.id")
    @Mapping(target = "licenseCategory", source = "licensecategory.id")
    @Mapping(target = "totalDutyMinutes", source = "totaldutyminute")
    CrewFact toCrewFact(Driver driver);

    @Mapping(target = "familiarityLevel", source = "routefamiliaritylevel.id")
    @Mapping(target = "licenseCategory", ignore = true)
    @Mapping(target = "totalDutyMinutes", source = "totaldutyminute")
    CrewFact toCrewFact(Conductor conductor);

    @Mapping(target = "busType", source = "bustype.name")
    @Mapping(target = "mileage", source = "mileage")
    VehicleFact toVehicleFact(Vehicle vehicle);


}
