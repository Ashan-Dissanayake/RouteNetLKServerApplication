package lk.ashan.routenetlkserverapllication.module.permit.repository;


import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
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
        scripts = "/sql/permit-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class PermitRepositoryTest extends BaseTestContainer{
    @Autowired
    private PermitRepository permitRepository;


    @Test
    void existsByNumber_shouldReturnTrue_whenPermitNumberExists() {

        boolean result =
                permitRepository.existsByNumber("12901");

        assertThat(result).isTrue();
    }


    @Test
    void existsByNumber_shouldReturnFalse_whenPermitNumberDoesNotExist() {

        boolean result =
                permitRepository.existsByNumber("NON_EXISTING");

        assertThat(result).isFalse();
    }


    @Test
    void existsByVehicleIdAndRouteIdAndPermitestatusId_shouldReturnTrue_whenMatchingPermitExists() {

        boolean result =
                permitRepository.existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(
                        9001,
                        9001,
                        1
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByVehicleIdAndRouteIdAndPermitestatusId_shouldReturnFalse_whenVehicleDoesNotMatch() {

        boolean result =
                permitRepository.existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(
                        9999,
                        9001,
                        1
                );

        assertThat(result).isFalse();
    }


    @Test
    void existsByVehicleIdAndRouteIdAndPermitestatusId_shouldReturnFalse_whenRouteDoesNotMatch() {

        boolean result =
                permitRepository.existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(
                        9001,
                        9999,
                        1
                );

        assertThat(result).isFalse();
    }


    @Test
    void existsByVehicleIdAndRouteIdAndPermitestatusId_shouldReturnFalse_whenStatusDoesNotMatch() {

        boolean result =
                permitRepository.existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(
                        9001,
                        9001,
                        2
                );

        assertThat(result).isTrue();
    }


    @Test
    void findByPermitestatusNameAndDoexpiredBefore_shouldReturnMatchingPermits() {

        List<Permite> permits =
                permitRepository.findByPermitestatus_NameAndDoexpiredBefore(
                        "Active",
                        LocalDate.of(2026, 6, 1)
                );

        assertThat(permits)
                .extracting(Permite::getId)
                .containsExactlyInAnyOrder(9101);
    }


    @Test
    void findByPermitestatusNameAndDoexpiredBefore_shouldExcludeDifferentStatus() {

        List<Permite> permits =
                permitRepository.findByPermitestatus_NameAndDoexpiredBefore(
                        "Active",
                        LocalDate.of(2026, 6, 1)
                );

        assertThat(permits)
                .extracting(Permite::getId)
                .doesNotContain(9104);
    }


    @Test
    void findByPermitestatusNameAndDoexpiredBetween_shouldReturnMatchingPermits() {

        List<Permite> permits =
                permitRepository.findByPermitestatus_NameAndDoexpiredBetween(
                        "Active",
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 7, 31)
                );

        assertThat(permits)
                .extracting(Permite::getId)
                .containsExactlyInAnyOrder(9102, 9103);
    }


    @Test
    void findByPermitestatusNameAndDoexpiredBetween_shouldExcludeOutsideRange() {

        List<Permite> permits =
                permitRepository.findByPermitestatus_NameAndDoexpiredBetween(
                        "Active",
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 7, 31)
                );

        assertThat(permits)
                .extracting(Permite::getId)
                .doesNotContain(9101, 9104);
    }


    @Test
    void findByPermitestatusNameAndDoexpiredBetween_shouldExcludeDifferentStatus() {

        List<Permite> permits =
                permitRepository.findByPermitestatus_NameAndDoexpiredBetween(
                        "Active",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2026, 12, 31)
                );

        assertThat(permits)
                .extracting(Permite::getId)
                .doesNotContain(9104);
    }
}
