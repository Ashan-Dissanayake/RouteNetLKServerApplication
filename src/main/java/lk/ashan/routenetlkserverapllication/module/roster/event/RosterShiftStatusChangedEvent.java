package lk.ashan.routenetlkserverapllication.module.roster.event;

public record RosterShiftStatusChangedEvent(
        Integer assignmentId,
        Integer employeeId,
        Integer branchId,
        String crewType,
        String previousStatus,
        String newStatus
) {
}