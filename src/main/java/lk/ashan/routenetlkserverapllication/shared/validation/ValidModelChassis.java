package lk.ashan.routenetlkserverapllication.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ModelChassisValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidModelChassis {
    String message() default "Chassis number does not match model";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
