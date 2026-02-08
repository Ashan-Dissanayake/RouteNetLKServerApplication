package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.permit.model.Route;
import lk.ashan.routenetlkserverapllication.module.permit.model.Routetype;
import lk.ashan.routenetlkserverapllication.module.permit.model.Servicetype;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Bustype;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PermitValidationContext {
   private Integer permitId;
   private String permitNumber;

   private Integer vehicleId;
   private Integer routeId;

   private Integer vehicleBranchId;
   private Integer requestBranchId;


   private Bustype busType;
   private Routetype routeType;
   private Servicetype serviceType;

   private LocalDate doissued;
   private LocalDate doexpired;

}
