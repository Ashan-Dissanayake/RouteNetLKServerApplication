package lk.ashan.routenetlkserverapllication.module.partreqest.repository;



import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/part-request-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class PartRequestRepositoryTest extends BaseTestContainer {

    @Autowired
    private PartRequestRepository partRequestRepository;


    // ============================================================
    // MATCHING REQUEST
    // ============================================================

    @Test
    void existsByBranchAndPartAndStatusInAndDoRequested_ShouldReturnTrue_WhenMatchingRequestExists() {

        boolean result =
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                9101,
                                9101,
                                List.of("Pending"),
                                LocalDate.of(2026, 8, 15)
                        );

        assertThat(result).isTrue();
    }


    // ============================================================
    // WRONG BRANCH
    // ============================================================

    @Test
    void existsByBranchAndPartAndStatusInAndDoRequested_ShouldReturnFalse_WhenBranchDoesNotMatch() {

        boolean result =
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                9999,
                                9101,
                                List.of("Pending"),
                                LocalDate.of(2026, 8, 15)
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // WRONG PART
    // ============================================================

    @Test
    void existsByBranchAndPartAndStatusInAndDoRequested_ShouldReturnFalse_WhenPartDoesNotMatch() {

        boolean result =
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                9101,
                                9999,
                                List.of("Pending"),
                                LocalDate.of(2026, 8, 15)
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // WRONG STATUS
    // ============================================================

    @Test
    void existsByBranchAndPartAndStatusInAndDoRequested_ShouldReturnFalse_WhenStatusDoesNotMatch() {

        boolean result =
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                9101,
                                9101,
                                List.of("Completed"),
                                LocalDate.of(2026, 8, 15)
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // WRONG REQUEST DATE
    // ============================================================

    @Test
    void existsByBranchAndPartAndStatusInAndDoRequested_ShouldReturnFalse_WhenRequestDateDoesNotMatch() {

        boolean result =
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                9101,
                                9101,
                                List.of("Pending"),
                                LocalDate.of(2026, 8, 16)
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // MULTIPLE STATUSES
    // ============================================================

    @Test
    void existsByBranchAndPartAndStatusInAndDoRequested_ShouldReturnTrue_WhenAnyProvidedStatusMatches() {

        boolean result =
                partRequestRepository
                        .existsByBranchAndPartAndStatusInAndDoRequested(
                                9101,
                                9101,
                                List.of("Completed", "Pending", "Cancelled"),
                                LocalDate.of(2026, 8, 15)
                        );

        assertThat(result).isTrue();
    }
}
