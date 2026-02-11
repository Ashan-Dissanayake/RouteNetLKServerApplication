package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.permit.model.Routetype;
import lk.ashan.routenetlkserverapllication.module.permit.model.Servicetype;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Bustype;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PermitValidationContext {
   private final Integer permitId;
   private final String permitNumber;

   private final Integer vehicleId;
   private final Integer routeId;

   private final Integer vehicleBranchId;
   private final Integer requestBranchId;

   private final Bustype busType;
   private final Routetype routeType;
   private final Servicetype serviceType;

   private final LocalDate doissued;
   private final LocalDate doexpired;

}
