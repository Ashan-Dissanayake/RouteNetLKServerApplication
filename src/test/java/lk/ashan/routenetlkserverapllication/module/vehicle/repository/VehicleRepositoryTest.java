package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = "/sql/vehicle-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class VehicleRepositoryTest extends BaseTestContainer {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void existsByNumber_shouldReturnTrue_whenVehicleNumberExists() {

        boolean result =
                vehicleRepository.existsByNumber("PRT9001");

        assertThat(result).isTrue();
    }

    @Test
    void existsByNumber_shouldReturnFalse_whenVehicleNumberDoesNotExist() {

        boolean result =
                vehicleRepository.existsByNumber("NOTEXIST");

        assertThat(result).isFalse();
    }

    @Test
    void findByMyId_shouldReturnVehicle() {

        Vehicle vehicle =
                vehicleRepository.findByMyId(9001);

        assertThat(vehicle).isNotNull();
        assertThat(vehicle.getId()).isEqualTo(9001);
        assertThat(vehicle.getNumber()).isEqualTo("PRT9001");
    }

    @Test
    void findByBranchId_shouldReturnVehiclesBelongingToBranch() {

        List<Vehicle> vehicles =
                vehicleRepository.findByBranch_Id(9001);

        assertThat(vehicles)
                .extracting(Vehicle::getId)
                .containsExactlyInAnyOrder(9001, 9002);
    }

    @Test
    void removeAll_shouldMarkSelectedVehiclesAsDeleted() {

        vehicleRepository.removeAll(List.of(9001, 9002));

        Vehicle vehicle1 = vehicleRepository.findByMyId(9001);
        Vehicle vehicle2 = vehicleRepository.findByMyId(9002);

        assertThat(vehicle1.getDeleted()).isTrue();
        assertThat(vehicle2.getDeleted()).isTrue();
    }
}

