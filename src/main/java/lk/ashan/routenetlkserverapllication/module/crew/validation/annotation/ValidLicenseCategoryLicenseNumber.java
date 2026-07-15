package lk.ashan.routenetlkserverapllication.module.crew.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to validate that the license number matches the allowed bus type
 * based on the license category. This annotation is applied at the class level.
 *
 * The validation logic is implemented in the {@code LicenseCategoryLicenseNumberValidator} class.
 */
@Constraint(validatedBy = LicenseCategoryLicenseNumberValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLicenseCategoryLicenseNumber {

    /**
     * Specifies the default error message when validation fails.
     *
     * @return the error message
     */
    String message() default "License Number does not match allowed bus type";

    /**
     * Specifies the validation groups the constraint belongs to.
     *
     * @return an array of validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Specifies the payload for clients to associate metadata with a constraint.
     *
     * @return an array of payload classes
     */
    Class<? extends Payload>[] payload() default {};
}
