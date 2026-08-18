package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/roster-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class RosterRepositoryTest extends BaseTestContainer {

    @Autowired
    private RosterRepository rosterRepository;


    // ========================================================
    // OVERLAPPING DATE RANGE
    // ========================================================

    @Test
    void existsOverlappingRoster_shouldReturnTrue_whenDateRangeOverlaps() {

        boolean result =
                rosterRepository.existsOverlappingRoster(
                        9001,
                        LocalDate.of(2026, 8, 12),
                        LocalDate.of(2026, 8, 18)
                );

        assertThat(result).isTrue();
    }


    // ========================================================
    // DATE RANGE COMPLETELY BEFORE
    // ========================================================

    @Test
    void existsOverlappingRoster_shouldReturnFalse_whenDateRangeIsBeforeExistingRoster() {

        boolean result = rosterRepository.existsOverlappingRoster(
                        9001,
                        LocalDate.of(2026, 7, 27),
                        LocalDate.of(2026, 8, 9)
                );

        assertThat(result).isFalse();
    }


    // ========================================================
    // DATE RANGE COMPLETELY AFTER
    // ========================================================

    @Test
    void existsOverlappingRoster_shouldReturnFalse_whenDateRangeIsAfterExistingRoster() {

        boolean result =
                rosterRepository.existsOverlappingRoster(
                        9001,
                        LocalDate.of(2026, 8, 17),
                        LocalDate.of(2026, 8, 23)
                );

        assertThat(result).isFalse();
    }


    // ========================================================
    // SAME DATE RANGE
    // ========================================================

    @Test
    void existsOverlappingRoster_shouldReturnTrue_whenDateRangeIsExactlySame() {

        boolean result =
                rosterRepository.existsOverlappingRoster(
                        9001,
                        LocalDate.of(2026, 8, 10),
                        LocalDate.of(2026, 8, 16)
                );

        assertThat(result).isTrue();
    }


    // ========================================================
    // EXISTING ROSTER INSIDE REQUESTED RANGE
    // ========================================================

    @Test
    void existsOverlappingRoster_shouldReturnTrue_whenExistingRosterIsInsideRequestedRange() {

        boolean result =
                rosterRepository.existsOverlappingRoster(
                        9001,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        assertThat(result).isTrue();
    }


    // ========================================================
    // DIFFERENT BRANCH
    // ========================================================

    @Test
    void existsOverlappingRoster_shouldReturnFalse_whenBranchDoesNotMatch() {

        boolean result =
                rosterRepository.existsOverlappingRoster(
                        9999,
                        LocalDate.of(2026, 8, 12),
                        LocalDate.of(2026, 8, 18)
                );

        assertThat(result).isFalse();
    }
}
