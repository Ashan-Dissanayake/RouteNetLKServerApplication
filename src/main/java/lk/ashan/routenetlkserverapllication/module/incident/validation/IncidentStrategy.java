package lk.ashan.routenetlkserverapllication.module.incident.validation;

public interface IncidentStrategy {
    void validate(IncidentContext context);
    boolean isApplicable(String typeCode);
}
