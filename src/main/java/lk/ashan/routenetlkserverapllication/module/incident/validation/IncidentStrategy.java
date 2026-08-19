package lk.ashan.routenetlkserverapllication.module.incident.validation;

/**
 * Interface for defining strategies to validate incidents based on their context and type.
 */
public interface IncidentStrategy {

    /**
     * Validates the given incident context.
     *
     * @param context the incident context to validate
     * @throws IllegalArgumentException if the context is invalid
     */
    void validate(IncidentContext context);

    /**
     * Determines if the strategy is applicable for the given type code.
     *
     * @param typeCode the type code to check applicability for
     * @return true if the strategy is applicable, false otherwise
     */
    boolean isApplicable(String typeCode);
}
