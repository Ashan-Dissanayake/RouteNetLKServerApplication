package lk.ashan.routenetlkserverapllication.module.roster.event;

public record RosterAssignmentConfirmedEvent(
        Integer assignmentId,
        Integer employeeId
) {}
