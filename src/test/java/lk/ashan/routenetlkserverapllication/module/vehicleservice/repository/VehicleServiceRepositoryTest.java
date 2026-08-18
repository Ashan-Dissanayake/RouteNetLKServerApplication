package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;


import lk.ashan.routenetlkserverapllication.report.model.projection.Report3Projection;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = "/sql/vehicle-service-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class VehicleServiceRepositoryTest extends BaseTestContainer {

    @Autowired
    private VehicleServiceRepository vehicleServiceRepository;


    // ============================================================
    // getMaintenanceLifecycleMetrics()
    // ============================================================

    @Test
    void getMaintenanceLifecycleMetrics_shouldReturnLastSevenWeeks() {

        List<Report3Projection> result =
                vehicleServiceRepository.getMaintenanceLifecycleMetrics();

        assertThat(result)
                .hasSize(7);

        assertThat(result)
                .extracting(Report3Projection::getWeekLabel)
                .containsExactly(
                        "Week 32",
                        "Week 31",
                        "Week 30",
                        "Week 29",
                        "Week 28",
                        "Week 27",
                        "Week 26"
                );
    }


    @Test
    void getMaintenanceLifecycleMetrics_shouldReturnCorrectCompletedAndPendingCounts() {

        List<Report3Projection> result =
                vehicleServiceRepository.getMaintenanceLifecycleMetrics();

        assertThat(result).hasSize(7);

        // --------------------------------------------------------
        // Week 32
        // Completed = 2
        // Pending   = 1
        // --------------------------------------------------------

        Report3Projection week32 = result.get(0);

        assertThat(week32.getWeekLabel())
                .isEqualTo("Week 32");

        assertThat(week32.getCompletedServices())
                .isEqualTo(2L);

        assertThat(week32.getPendingBacklog())
                .isEqualTo(1L);


        // --------------------------------------------------------
        // Week 31
        // Completed = 1
        // Pending   = 2
        // --------------------------------------------------------

        Report3Projection week31 = result.get(1);

        assertThat(week31.getWeekLabel())
                .isEqualTo("Week 31");

        assertThat(week31.getCompletedServices())
                .isEqualTo(1L);

        assertThat(week31.getPendingBacklog())
                .isEqualTo(2L);


        // --------------------------------------------------------
        // Week 30
        // Completed = 0
        // Pending   = 1
        // --------------------------------------------------------

        Report3Projection week30 = result.get(2);

        assertThat(week30.getWeekLabel())
                .isEqualTo("Week 30");

        assertThat(week30.getCompletedServices())
                .isEqualTo(0L);

        assertThat(week30.getPendingBacklog())
                .isEqualTo(1L);


        // --------------------------------------------------------
        // Week 29
        // Completed = 1
        // Pending   = 0
        // --------------------------------------------------------

        Report3Projection week29 = result.get(3);

        assertThat(week29.getWeekLabel())
                .isEqualTo("Week 29");

        assertThat(week29.getCompletedServices())
                .isEqualTo(1L);

        assertThat(week29.getPendingBacklog())
                .isEqualTo(0L);


        // --------------------------------------------------------
        // Week 28
        // Completed = 1
        // Pending   = 1
        // --------------------------------------------------------

        Report3Projection week28 = result.get(4);

        assertThat(week28.getWeekLabel())
                .isEqualTo("Week 28");

        assertThat(week28.getCompletedServices())
                .isEqualTo(1L);

        assertThat(week28.getPendingBacklog())
                .isEqualTo(1L);


        // --------------------------------------------------------
        // Week 27
        // Completed = 2
        // Pending   = 0
        // --------------------------------------------------------

        Report3Projection week27 = result.get(5);

        assertThat(week27.getWeekLabel())
                .isEqualTo("Week 27");

        assertThat(week27.getCompletedServices())
                .isEqualTo(2L);

        assertThat(week27.getPendingBacklog())
                .isEqualTo(0L);


        // --------------------------------------------------------
        // Week 26
        // Completed = 0
        // Pending   = 1
        // --------------------------------------------------------

        Report3Projection week26 = result.get(6);

        assertThat(week26.getWeekLabel())
                .isEqualTo("Week 26");

        assertThat(week26.getCompletedServices())
                .isEqualTo(0L);

        assertThat(week26.getPendingBacklog())
                .isEqualTo(1L);
    }


    @Test
    void getMaintenanceLifecycleMetrics_shouldOrderWeeksDescending() {

        List<Report3Projection> result =
                vehicleServiceRepository.getMaintenanceLifecycleMetrics();

        assertThat(result)
                .extracting(Report3Projection::getWeekLabel)
                .containsExactly(
                        "Week 32",
                        "Week 31",
                        "Week 30",
                        "Week 29",
                        "Week 28",
                        "Week 27",
                        "Week 26"
                );
    }

    @Test
    void debugMaintenanceData() {

        List<Report3Projection> result =
                vehicleServiceRepository.getMaintenanceLifecycleMetrics();

        result.forEach(row ->
                System.out.println(
                        row.getWeekLabel()
                                + " | completed=" + row.getCompletedServices()
                                + " | pending=" + row.getPendingBacklog()
                )
        );
    }
}
