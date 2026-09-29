package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.ServiceType;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PermitValidator {

    private static final Integer ACTIVE_STATUS_ID = 1;

    private final PermitRepository permitRepository;

    public void validateCreate(
            Vehicle vehicle,
            Route route,
            ServiceType serviceType
    ) {
        validateActivePermitUniqueness(
                vehicle.getId(),
                route.getId()
        );

        validateBusTypeAndRouteType(
                vehicle,
                route
        );

        validateBusTypeAndServiceType(
                vehicle,
                serviceType
        );
    }

    private void validateActivePermitUniqueness(
            Integer vehicleId,
            Integer routeId
    ) {
        boolean exists =
                permitRepository
                        .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(
                                vehicleId,
                                routeId,
                                ACTIVE_STATUS_ID
                        );

        if (exists) {
            throw new ResourceExistsException(
                    String.format(
                            "Vehicle %d already has an active permit for route %d",
                            vehicleId,
                            routeId
                    )
            );
        }
    }

    private void validateBusTypeAndRouteType(
            Vehicle vehicle,
            Route route
    ) {
        String busType =
                vehicle.getBustype()
                        .getName()
                        .trim()
                        .toUpperCase();

        String routeType =
                route.getRoutetype()
                        .getName()
                        .trim()
                        .toLowerCase();

        List<String> allowedBusTypes =
                VALID_ROUTE_BUS_TYPES.get(routeType);

        if (allowedBusTypes == null
                || !allowedBusTypes.contains(busType)) {

            throw new BusinessRuleViolationException(
                    String.format(
                            "Invalid combination: Type %s buses cannot be used on %s route.",
                            busType,
                            routeType
                    )
            );
        }
    }

    private void validateBusTypeAndServiceType(
            Vehicle vehicle,
            ServiceType serviceType
    ) {
        String busType =
                vehicle.getBustype()
                        .getName()
                        .trim()
                        .toUpperCase();

        String serviceTypeName =
                serviceType.getName()
                        .trim()
                        .toLowerCase();

        List<String> allowedBusTypes =
                VALID_SERVICE_BUS_TYPES.get(serviceTypeName);

        if (allowedBusTypes == null
                || !allowedBusTypes.contains(busType)) {

            throw new BusinessRuleViolationException(
                    String.format(
                            "Invalid combination: %s cannot be used for %s service.",
                            busType,
                            serviceTypeName
                    )
            );
        }
    }

    public void validateInitialStatus(
            PermiteStatus status
    ) {
        if (!"ACTIVE".equalsIgnoreCase(status.getName())) {
            throw new BusinessRuleViolationException(
                    "New permits must start with ACTIVE status"
            );
        }
    }

    public void validateStatusTransition(
            PermiteStatus currentStatus,
            PermiteStatus targetStatus
    ) {
        String current =
                normalize(currentStatus.getName());

        String target =
                normalize(targetStatus.getName());

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "ACTIVE" ->
                    target.equals("EXPIRED")
                            || target.equals("SUSPENDED")
                            || target.equals("TRANSFERRED");

            case "SUSPENDED" ->
                    target.equals("ACTIVE")
                            || target.equals("EXPIRED")
                            || target.equals("TRANSFERRED");

            case "EXPIRED",
                 "TRANSFERRED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new BusinessRuleViolationException(
                    "Invalid permit status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    private String normalize(String value) {
        return value.trim().toUpperCase();
    }

    private static final Map<String, List<String>>
            VALID_ROUTE_BUS_TYPES = Map.of(

            "inter provincial",
            List.of(
                    "AA",
                    "A",
                    "B+",
                    "B"
            ),

            "intra provincial",
            List.of(
                    "AA",
                    "A",
                    "B+",
                    "B",
                    "C",
                    "D",
                    "E"
            )
    );

    private static final Map<String, List<String>>
            VALID_SERVICE_BUS_TYPES = Map.of(

            "luxury",
            List.of("AA"),

            "super luxury",
            List.of("AA"),

            "normal",
            List.of(
                    "A",
                    "A+",
                    "B",
                    "B+",
                    "C",
                    "D",
                    "E"
            ),

            "semi luxury",
            List.of(
                    "A",
                    "A+",
                    "B",
                    "B+"
            )
    );
}