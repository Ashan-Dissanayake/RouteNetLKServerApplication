package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository;


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
        scripts = "/sql/incident-vehicle-allocation-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class IncidentVehicleAllocationRepositoryTest extends BaseTestContainer {

    @Autowired
    private IncidentVehicleAllocationRepository incidentVehicleAllocationRepository;


    // ============================================================
    // existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn
    // ============================================================

    @Test
    void existsByVehicleIdAndStatusNames_ShouldReturnTrue_WhenMatchingAllocationExists() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                List.of("Assigned")
                        );

        assertThat(result).isTrue();
    }


    @Test
    void existsByVehicleIdAndStatusNames_ShouldReturnFalse_WhenStatusDoesNotMatch() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                List.of("Released")
                        );

        assertThat(result).isFalse();
    }


    @Test
    void existsByVehicleIdAndStatusNames_ShouldReturnFalse_WhenVehicleDoesNotExist() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9999,
                                List.of("Assigned")
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByIncident_IdAndVehicle_IdAndStatusNames
    // ============================================================

    @Test
    void existsByIncidentIdAndVehicleIdAndStatusNames_ShouldReturnTrue_WhenMatchingAllocationExists() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                9001,
                                List.of("Assigned")
                        );

        assertThat(result).isTrue();
    }


    @Test
    void existsByIncidentIdAndVehicleIdAndStatusNames_ShouldReturnFalse_WhenStatusDoesNotMatch() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                9001,
                                List.of("Released")
                        );

        assertThat(result).isFalse();
    }


    @Test
    void existsByIncidentIdAndVehicleIdAndStatusNames_ShouldReturnFalse_WhenVehicleDoesNotMatch() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                9999,
                                List.of("Assigned")
                        );

        assertThat(result).isFalse();
    }


    @Test
    void existsByIncidentIdAndVehicleIdAndStatusNames_ShouldReturnFalse_WhenIncidentDoesNotExist() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9999,
                                9001,
                                List.of("Assigned")
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn
    // ============================================================

    @Test
    void existsByIncidentIdAndStatusNames_ShouldReturnTrue_WhenMatchingAllocationExists() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                List.of("Assigned")
                        );

        assertThat(result).isTrue();
    }


    @Test
    void existsByIncidentIdAndStatusNames_ShouldReturnFalse_WhenStatusDoesNotMatch() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                List.of("Released")
                        );

        assertThat(result).isFalse();
    }


    @Test
    void existsByIncidentIdAndStatusNames_ShouldReturnFalse_WhenIncidentDoesNotExist() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                                9999,
                                List.of("Assigned")
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // Multiple statuses
    // ============================================================

    @Test
    void existsByVehicleIdAndStatusNames_ShouldReturnTrue_WhenAnyProvidedStatusMatches() {

        boolean result =
                incidentVehicleAllocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                9001,
                                List.of("Released", "Assigned")
                        );

        assertThat(result).isTrue();
    }
}
