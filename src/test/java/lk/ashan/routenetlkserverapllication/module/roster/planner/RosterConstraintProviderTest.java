package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;


class RosterConstraintProviderTest {

    private ConstraintVerifier<RosterConstraintProvider, RosterSolution> constraintVerifier;

    @BeforeEach
    void setUp() {
        constraintVerifier = ConstraintVerifier.build(
                new RosterConstraintProvider(),
                RosterSolution.class,
                Rosterassignement.class
        );
    }

    @Test
    void employeeAssignedOnlyOnce_shouldPenalizeDuplicateAssignments() {

        Employee emp = new Employee();
        emp.setId(1);

        Roster roster = new Roster();
        roster.setId(1);

        Rosterassignement a1 = new Rosterassignement();
        a1.setId(1);
        a1.setEmployee(emp);
        a1.setRoster(roster);

        Rosterassignement a2 = new Rosterassignement();
        a2.setId(2);
        a2.setEmployee(emp);
        a2.setRoster(roster);

        constraintVerifier.verifyThat(RosterConstraintProvider::employeeAssignedOnlyOnce)
                .given(
                        roster,   // problem fact
                        emp,      // problem fact
                        a1, a2    // planning entities
                )
                .penalizesBy(1);
    }


}
