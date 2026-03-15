package lk.ashan.routenetlkserverapllication.module.permit.validation;

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

   private final Integer requestBranchId;

   //private final Integer busTypeId;
   //private final Integer routeTypeId;
   private final Integer serviceTypeId;

   private final LocalDate doissued;
   private final LocalDate doexpired;

}
