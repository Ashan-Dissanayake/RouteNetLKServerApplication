package lk.ashan.routenetlkserverapllication.module.crew.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LicenseCategoryLicenseNumberValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLicenseCategoryLicenseNumber {
    String message() default "License Number does not match allowed bus type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
