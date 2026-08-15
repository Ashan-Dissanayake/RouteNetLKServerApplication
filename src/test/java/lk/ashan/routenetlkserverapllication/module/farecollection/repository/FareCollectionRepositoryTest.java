package lk.ashan.routenetlkserverapllication.module.farecollection.repository;

import lk.ashan.routenetlkserverapllication.report.model.projection.Report2Projection;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/fare-collection-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class FareCollectionRepositoryTest extends BaseTestContainer {

    @Autowired
    private FareCollectionRepository fareCollectionRepository;


    // ============================================================
    // existsByTripexecution_Id
    // ============================================================

    @Test
    void existsByTripexecutionId_ShouldReturnTrue_WhenFareCollectionExists() {

        boolean result =
                fareCollectionRepository.existsByTripexecution_Id(9001);

        assertThat(result).isTrue();
    }


    @Test
    void existsByTripexecutionId_ShouldReturnFalse_WhenFareCollectionDoesNotExist() {

        boolean result =
                fareCollectionRepository.existsByTripexecution_Id(9999);

        assertThat(result).isFalse();
    }


    // ============================================================
    // getDepotFinancialReconciliationMetrics
    // ============================================================

    @Test
    void getDepotFinancialReconciliationMetrics_ShouldReturnDepotFinancialData() {

        List<Report2Projection> result =
                fareCollectionRepository
                        .getDepotFinancialReconciliationMetrics();

        assertThat(result)
                .isNotEmpty();

        Report2Projection projection =
                result.stream()
                        .filter(item ->
                                "Test Colombo Depot"
                                        .equals(item.getDepotName()))
                        .findFirst()
                        .orElseThrow();

        assertThat(projection.getDepotName())
                .isEqualTo("Test Colombo Depot");

        assertThat(projection.getCashAmount())
                .isEqualByComparingTo("7500.00");

        assertThat(projection.getDigitalAmount())
                .isEqualByComparingTo("2500.00");
    }


    // ============================================================
    // getDailyRevenueSummaryByBranch
    // ============================================================

    @Test
    void getDailyRevenueSummaryByBranch_ShouldReturnDailyRevenueSummary() {

        Object[] result =
                fareCollectionRepository
                        .getDailyRevenueSummaryByBranch(9001);

        assertThat(result)
                .isNotNull()
                .hasSize(1);

        Object[] summary = (Object[]) result[0];

        assertThat(summary)
                .hasSize(3);

        assertThat(((Number) summary[0]).intValue())
                .isEqualTo(100);

        assertThat((BigDecimal) summary[1])
                .isEqualByComparingTo("7500.00");

        assertThat((BigDecimal) summary[2])
                .isEqualByComparingTo("2500.00");
    }

    @Test
    void getDailyRevenueSummaryByBranch_ShouldReturnZero_WhenNoFareCollectionExistsForBranch() {

        Object[] result =
                fareCollectionRepository
                        .getDailyRevenueSummaryByBranch(9999);

        assertThat(result)
                .isNotNull()
                .hasSize(1);

        Object[] summary = (Object[]) result[0];

        assertThat(summary)
                .hasSize(3);

        assertThat(((Number) summary[0]).intValue())
                .isZero();

        assertThat((BigDecimal) summary[1])
                .isEqualByComparingTo(BigDecimal.ZERO);

        assertThat((BigDecimal) summary[2])
                .isEqualByComparingTo(BigDecimal.ZERO);
    }
}
