package lk.ashan.routenetlkserverapllication.module.trip.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DepartureArrivalValidator.class)
@Documented
public @interface ValidTimeRange {

    String message() default "Departure time must be before arrival time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
