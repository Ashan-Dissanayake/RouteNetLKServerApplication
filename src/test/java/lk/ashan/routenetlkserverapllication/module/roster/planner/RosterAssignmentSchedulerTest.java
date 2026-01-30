package lk.ashan.routenetlkserverapllication.module.roster.planner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RosterAssignmentSchedulerTest {

    @Autowired
    private RosterAssignmentScheduler rosterAssignmentScheduler;

    @Test
    void manualRosterAssignmentTest() {
        Integer branchId = 1;
        LocalDate date = LocalDate.parse("2026-02-02");

        rosterAssignmentScheduler.manualAssignment(branchId, date);
    }

}
