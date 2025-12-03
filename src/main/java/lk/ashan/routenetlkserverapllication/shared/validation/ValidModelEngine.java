package lk.ashan.routenetlkserverapllication.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ModelEngineValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidModelEngine {
    String message() default "Engine number does not match model";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

