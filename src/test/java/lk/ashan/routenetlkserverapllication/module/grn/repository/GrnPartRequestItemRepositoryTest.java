package lk.ashan.routenetlkserverapllication.module.grn.repository;


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
        scripts = "/sql/grn-part-request-item-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GrnPartRequestItemRepositoryTest extends BaseTestContainer {

    @Autowired
    private GrnPartRequestItemRepository grnPartRequestItemRepository;


    // ============================================================
    // sumQuantityByPartRequestItemId
    // ============================================================

    @Test
    void sumQuantityByPartRequestItemId_ShouldReturnSumForMatchingStatus() {

        BigDecimal result =
                grnPartRequestItemRepository
                        .sumQuantityByPartRequestItemId(
                                9001,
                                List.of("Received")
                        );

        assertThat(result)
                .isEqualByComparingTo("85.00");
    }


    @Test
    void sumQuantityByPartRequestItemId_ShouldIgnoreNonMatchingStatuses() {

        BigDecimal result =
                grnPartRequestItemRepository
                        .sumQuantityByPartRequestItemId(
                                9001,
                                List.of("Received")
                        );

        /*
         * Received:
         *   GRN 9001 -> 60
         *   GRN 9003 -> 25
         *
         * Draft:
         *   GRN 9002 -> 40
         *
         * Expected:
         *   60 + 25 = 85
         */
        assertThat(result)
                .isEqualByComparingTo("85.00");

        assertThat(result)
                .isNotEqualByComparingTo("125.00");
    }


    @Test
    void sumQuantityByPartRequestItemId_ShouldReturnSumForMultipleStatuses() {

        BigDecimal result =
                grnPartRequestItemRepository
                        .sumQuantityByPartRequestItemId(
                                9001,
                                List.of("Received", "Draft")
                        );

        /*
         * Received -> 60 + 25
         * Draft    -> 40
         *
         * Total = 125
         */
        assertThat(result)
                .isEqualByComparingTo("125.00");
    }


    @Test
    void sumQuantityByPartRequestItemId_ShouldReturnNull_WhenNoMatchingStatusExists() {

        BigDecimal result =
                grnPartRequestItemRepository
                        .sumQuantityByPartRequestItemId(
                                9001,
                                List.of("Cancelled")
                        );

        /*
         * Current repository query does NOT use COALESCE.
         *
         * Therefore SUM() over no matching rows returns null.
         */
        assertThat(result)
                .isNull();
    }


    @Test
    void sumQuantityByPartRequestItemId_ShouldReturnNull_WhenPartRequestItemDoesNotExist() {

        BigDecimal result =
                grnPartRequestItemRepository
                        .sumQuantityByPartRequestItemId(
                                9999,
                                List.of("Received")
                        );

        assertThat(result)
                .isNull();
    }


    @Test
    void sumQuantityByPartRequestItemId_ShouldReturnNull_WhenStatusListIsEmpty() {

        BigDecimal result =
                grnPartRequestItemRepository
                        .sumQuantityByPartRequestItemId(
                                9001,
                                List.of()
                        );

        assertThat(result)
                .isNull();
    }
}
