package lk.ashan.routenetlkserverapllication.module.roster.repository;


import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)@Sql(
        scripts = "/sql/roster-shift-assignment-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class RosterShiftAssignmentRepositoryTest extends BaseTestContainer {

    @Autowired
    private RosterShiftAssignmentRepository rosterShiftAssignmentRepository;


    // ============================================================
    // findUnassignedByRosterId()
    // ============================================================

    @Test
    void findUnassignedByRosterId_shouldReturnOnlyUnassignedAssignments() {

        List<RosterShiftAssignment> assignments =
                rosterShiftAssignmentRepository.findUnassignedByRosterId(9001);

        assertThat(assignments)
                .extracting(RosterShiftAssignment::getId)
                .containsExactly(9001, 9003)
                .doesNotContain(9002);
    }


    // ============================================================
    // findByRosterId()
    // ============================================================

    @Test
    void findByRosterId_shouldReturnAllAssignmentsForRoster() {

        List<RosterShiftAssignment> assignments =
                rosterShiftAssignmentRepository.findByRosterId(9001);

        assertThat(assignments)
                .extracting(RosterShiftAssignment::getId)
                .containsExactly(9001, 9002, 9003);
    }


    // ============================================================
    // updateEmployeeAndStatusDirectly()
    // ============================================================

    @Test
    void updateEmployeeAndStatusDirectly_shouldUpdateEmployeeAndStatus() {

        rosterShiftAssignmentRepository.updateEmployeeAndStatusDirectly(
                9001,
                9001,
                3
        );

        RosterShiftAssignment assignment =
                rosterShiftAssignmentRepository.findById(9001)
                        .orElseThrow();

        assertThat(assignment.getEmployee())
                .isNotNull();

        assertThat(assignment.getEmployee().getId())
                .isEqualTo(9001);

        assertThat(assignment.getRostershiftassignmentstatus().getId())
                .isEqualTo(3);
    }


    // ============================================================
    // countAssignmentsByRosterShiftId()
    // ============================================================

    @Test
    void countAssignmentsByRosterShiftId_shouldReturnCorrectCount() {

        int count =
                rosterShiftAssignmentRepository
                        .countAssignmentsByRosterShiftId(9001);

        assertThat(count)
                .isEqualTo(2);
    }


    @Test
    void countAssignmentsByRosterShiftId_shouldReturnCorrectCountForSecondShift() {

        int count =
                rosterShiftAssignmentRepository
                        .countAssignmentsByRosterShiftId(9002);

        assertThat(count)
                .isEqualTo(1);
    }
}
