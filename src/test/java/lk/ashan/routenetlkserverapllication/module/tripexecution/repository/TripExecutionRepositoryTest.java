package lk.ashan.routenetlkserverapllication.module.tripexecution.repository;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/tripexecution-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class TripExecutionRepositoryTest extends BaseTestContainer {

    @Autowired
    private TripExecutionRepository tripExecutionRepository;


    @Test
    void findAllByTripId_shouldReturnExecutionsForTrip() {

        Optional<List<TripExecution>> result =
                tripExecutionRepository.findAllByTrip_Id(9001);

        assertThat(result).isPresent();

        assertThat(result.get())
                .extracting(TripExecution::getId)
                .containsExactlyInAnyOrder(9001, 9002, 9003, 9004);
    }


    @Test
    void findByDoserviceAndBranchId_shouldReturnExecutionsForDateAndBranch() {

        List<TripExecution> executions =
                tripExecutionRepository.findByDoserviceAndBranch_Id(
                        LocalDate.of(2026, 8, 10),
                        9001
                );

        assertThat(executions)
                .extracting(TripExecution::getId)
                .containsExactlyInAnyOrder(9001, 9002);
    }


    @Test
    void findByDoserviceAndBranchId_shouldExcludeExecutionsFromOtherDate() {

        List<TripExecution> executions =
                tripExecutionRepository.findByDoserviceAndBranch_Id(
                        LocalDate.of(2026, 8, 10),
                        9001
                );

        assertThat(executions)
                .extracting(TripExecution::getId)
                .doesNotContain(9003);
    }


    @Test
    void findByDoserviceAndBranchIdAndDriverIsNull_shouldReturnUnassignedExecutions() {

        List<TripExecution> executions =
                tripExecutionRepository.findByDoserviceAndBranch_IdAndDriverIsNull(
                        LocalDate.of(2026, 8, 10),
                        9001
                );

        assertThat(executions)
                .extracting(TripExecution::getId)
                .containsExactlyInAnyOrder(9001, 9002);
    }


    @Test
    void findByDoserviceAndBranchIdAndDriverIsNull_shouldExcludeExecutionsWithDriver() {

        List<TripExecution> executions =
                tripExecutionRepository.findByDoserviceAndBranch_IdAndDriverIsNull(
                        LocalDate.of(2026, 8, 10),
                        9001
                );

        assertThat(executions)
                .allMatch(execution -> execution.getDriver() == null);
    }


    @Test
    void findByTripexecutionstatusName_shouldReturnExecutionsWithStatus() {

        List<TripExecution> executions =
                tripExecutionRepository.findByTripexecutionstatus_Name(
                        "Completed"
                );

        assertThat(executions)
                .extracting(TripExecution::getId)
                .containsExactlyInAnyOrder(9001, 9003);
    }


    @Test
    void findByTripexecutionstatusName_shouldReturnDispatchedExecutions() {

        List<TripExecution> executions =
                tripExecutionRepository.findByTripexecutionstatus_Name(
                        "Dispatched"
                );

        assertThat(executions)
                .extracting(TripExecution::getId)
                .containsExactlyInAnyOrder(9004);
    }


    @Test
    void findByTripexecutionstatusName_shouldReturnEmptyWhenStatusDoesNotExist() {

        List<TripExecution> executions =
                tripExecutionRepository.findByTripexecutionstatus_Name(
                        "Cancelled"
                );

        assertThat(executions).isEmpty();
    }


    @Test
    void countActiveTripsByBranch_shouldReturnDispatchedTripsForToday() {

        long count =
                tripExecutionRepository.countActiveTripsByBranch(9001);

        assertThat(count).isEqualTo(1);
    }


    @Test
    void countActiveTripsByBranch_shouldReturnZeroForBranchWithoutDispatchedTrips() {

        long count =
                tripExecutionRepository.countActiveTripsByBranch(9999);

        assertThat(count).isZero();
    }
}
