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
   private Integer permitId;
   private Bustype busType;
   private Routetype routeType;
   private Servicetype serviceType;
   private String permitNumber;
   private LocalDate doissued;
   private LocalDate doexpired;
}
