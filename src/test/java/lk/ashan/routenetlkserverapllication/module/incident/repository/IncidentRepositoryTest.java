package lk.ashan.routenetlkserverapllication.module.incident.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report5Projection;
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
)
@Sql(
        scripts = "/sql/incident-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class IncidentRepositoryTest extends BaseTestContainer {

    @Autowired
    private IncidentRepository incidentRepository;

    // ========================================================
    // REPORT 1
    // ========================================================

    @Test
    void getTripsCountByDay_ShouldReturnCompletedTripsGroupedByWeekday() {

        List<Object[]> results =
                incidentRepository.getTripsCountByDay(9);

        assertThat(results).hasSize(2);

        // 2026-08-10 = Monday -> WEEKDAY = 0
        assertThat(results)
                .anySatisfy(row -> {
                    assertThat(((Number) row[0]).intValue()).isEqualTo(0);
                    assertThat(((Number) row[1]).longValue()).isEqualTo(1);
                });

        // 2026-08-11 = Tuesday -> WEEKDAY = 1
        assertThat(results)
                .anySatisfy(row -> {
                    assertThat(((Number) row[0]).intValue()).isEqualTo(1);
                    assertThat(((Number) row[1]).longValue()).isEqualTo(1);
                });
    }

    @Test
    void getIncidentsCountByDay_ShouldReturnMechanicalBreakdownsGroupedByWeekday() {

        List<Object[]> results =
                incidentRepository.getIncidentsCountByDay(1);

        assertThat(results).hasSize(2);

        // Monday
        assertThat(results)
                .anySatisfy(row -> {
                    assertThat(((Number) row[0]).intValue()).isEqualTo(0);
                    assertThat(((Number) row[1]).longValue()).isEqualTo(1);
                });

        // Tuesday
        assertThat(results)
                .anySatisfy(row -> {
                    assertThat(((Number) row[0]).intValue()).isEqualTo(1);
                    assertThat(((Number) row[1]).longValue()).isEqualTo(1);
                });
    }

    // ========================================================
    // REPORT 5
    // ========================================================

    @Test
    void getIncidentDistributionMetrics_ShouldReturnIncidentCountsByType() {

        List<Report5Projection> results =
                incidentRepository.getIncidentDistributionMetrics();

        assertThat(results).hasSize(3);

        assertThat(results)
                .anySatisfy(result -> {
                    assertThat(result.getIncidentTypeName())
                            .isEqualTo("Mechanical Breakdown");

                    assertThat(result.getIncidentCount())
                            .isEqualTo(3L);
                });

        assertThat(results)
                .anySatisfy(result -> {
                    assertThat(result.getIncidentTypeName())
                            .isEqualTo("Accident");

                    assertThat(result.getIncidentCount())
                            .isEqualTo(1L);
                });

        assertThat(results)
                .anySatisfy(result -> {
                    assertThat(result.getIncidentTypeName())
                            .isEqualTo("Tyre Puncture");

                    assertThat(result.getIncidentCount())
                            .isEqualTo(1L);
                });
    }

    // ========================================================
    // DASHBOARD
    // ========================================================

    @Test
    void findActiveIncidentsByBranch_ShouldExcludeResolvedIncidents() {

        List<Incident> results =
                incidentRepository.findActiveIncidentsByBranch(9001);

        assertThat(results)
                .extracting(Incident::getId)
                .containsExactly(9005, 9003, 9002, 9001);

        assertThat(results)
                .extracting(Incident::getId)
                .doesNotContain(9004);
    }

    @Test
    void countPendingIncidentsByBranch_ShouldReturnPendingAllocationIncidentCount() {

        long result =
                incidentRepository.countPendingIncidentsByBranch(9001);

        assertThat(result).isEqualTo(2L);
    }
}
