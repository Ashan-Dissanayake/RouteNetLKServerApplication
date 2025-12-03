package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seatingcapacity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ModelSeatingValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidModelSeating {
    String message() default "Seating capacity does not match model";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
