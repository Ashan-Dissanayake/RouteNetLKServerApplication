package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.pattern;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusValidator.class)
@Documented
public @interface ValidBus {

    String message() default "Invalid chassis or engine number for the selected bus model";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
