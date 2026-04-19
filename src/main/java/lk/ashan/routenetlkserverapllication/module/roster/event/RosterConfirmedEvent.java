package lk.ashan.routenetlkserverapllication.module.roster.event;

import java.time.LocalDate;

public record RosterConfirmedEvent(LocalDate date, Integer branchId) {}
