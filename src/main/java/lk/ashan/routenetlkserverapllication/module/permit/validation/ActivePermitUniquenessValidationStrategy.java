package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivePermitUniquenessValidationStrategy implements PermitValidationStrategy{

    private static final Integer ACTIVE_STATUS_ID = 1;

    private final PermitRepository permitRepository;

    @Override
    public void validate(PermitValidationContext context) {
        if (context.getVehicleId() == null || context.getRouteId() == null) {
            return;
        }

        boolean exists = permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(
                        context.getVehicleId(),
                        context.getRouteId(),
                        ACTIVE_STATUS_ID
                );

        if (exists) {
            throw new ResourceExistsException(
                    String.format(
                            "Vehicle %d already has an active permit for route %d",
                            context.getVehicleId(),
                            context.getRouteId()
                    )
            );
        }
    }
}
