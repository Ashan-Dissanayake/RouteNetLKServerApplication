package lk.ashan.routenetlkserverapllication.module.trip.repository;


import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/trip-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class TripRepositoryTest extends BaseTestContainer {

    @Autowired
    private TripRepository tripRepository;


    // ============================================================
    // findByPermite_Route_Id
    // ============================================================

    @Test
    void findByPermiteRouteId_shouldReturnTripsForRoute() {

        List<Trip> trips =
                tripRepository.findByPermite_Route_Id(9001);

        assertThat(trips)
                .extracting(Trip::getId)
                .containsExactlyInAnyOrder(9001, 9002, 9004);
    }


    @Test
    void findByPermiteRouteId_shouldReturnEmpty_whenRouteHasNoTrips() {

        List<Trip> trips =
                tripRepository.findByPermite_Route_Id(9999);

        assertThat(trips).isEmpty();
    }


    // ============================================================
    // existsByPermite_IdAndOriginterminal_IdAndTodepature
    // ============================================================

    @Test
    void existsByPermitOriginDepartureArrivalAndStatus_shouldReturnTrue_whenMatchingTripExists() {

        boolean result =
                tripRepository
                        .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                                9001,
                                1,
                                LocalTime.of(8, 0),
                                LocalTime.of(10, 0),
                                "Active"
                        );

        assertThat(result).isTrue();
    }


    @Test
    void existsByPermitOriginDepartureArrivalAndStatus_shouldReturnFalse_whenStatusDoesNotMatch() {

        boolean result =
                tripRepository
                        .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                                9001,
                                1,
                                LocalTime.of(8, 0),
                                LocalTime.of(10, 0),
                                "Draft"
                        );

        assertThat(result).isFalse();
    }


    @Test
    void existsByPermitOriginDepartureArrivalAndStatus_shouldReturnFalse_whenTripDoesNotExist() {

        boolean result =
                tripRepository
                        .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                                9001,
                                1,
                                LocalTime.of(6, 0),
                                LocalTime.of(7, 0),
                                "Active"
                        );

        assertThat(result).isFalse();
    }


    // ============================================================
    // countByPermite_IdAndTripstatus_Name
    // ============================================================

    @Test
    void countByPermitIdAndStatus_shouldReturnActiveTripCount() {

        long count =
                tripRepository.countByPermite_IdAndTripstatus_Name(
                        9001,
                        "Active"
                );

        assertThat(count).isEqualTo(2);
    }


    @Test
    void countByPermitIdAndStatus_shouldReturnZero_whenStatusHasNoTrips() {

        long count =
                tripRepository.countByPermite_IdAndTripstatus_Name(
                        9001,
                        "Completed"
                );

        assertThat(count).isZero();
    }


    // ============================================================
    // findByPermite_Id
    // ============================================================

    @Test
    void findByPermiteId_shouldReturnAllTripsForPermit() {

        List<Trip> trips =
                tripRepository.findByPermite_Id(9001);

        assertThat(trips)
                .extracting(Trip::getId)
                .containsExactlyInAnyOrder(9001, 9002, 9004);
    }


    @Test
    void findByPermiteId_shouldReturnEmpty_whenPermitHasNoTrips() {

        List<Trip> trips =
                tripRepository.findByPermite_Id(9999);

        assertThat(trips).isEmpty();
    }


    // ============================================================
    // countDistinctPermitsForShift
    // ============================================================

    @Test
    void countDistinctPermitsForShift_shouldCountDistinctActivePermits() {

        long count =
                tripRepository.countDistinctPermitsForShift(
                        9001,
                        1
                );

        /*
         * Shift 1:
         * Trip 9001 -> Permit 9001
         *
         * Therefore distinct permits = 1
         */
        assertThat(count).isEqualTo(1);
    }


    @Test
    void countDistinctPermitsForShift_shouldReturnZero_whenNoActiveTripsExist() {

        long count =
                tripRepository.countDistinctPermitsForShift(
                        9001,
                        3
                );

        /*
         * Trip 9004 uses shift 3 but its status is Draft.
         * Query only counts Active trips.
         */
        assertThat(count).isZero();
    }


    // ============================================================
    // existsInterprovincialTripInShift
    // ============================================================

    @Test
    void existsInterprovincialTripInShift_shouldReturnTrue_whenTripExistsWithinShift() {

        boolean result =
                tripRepository.existsInterprovincialTripInShift(
                        9001,
                        LocalTime.of(8, 0),
                        LocalTime.of(16, 0)
                );

        /*
         * Trip 9003:
         * route 9002 -> routetype 2
         * departure = 09:00
         *
         * 09:00 >= 08:00
         * 09:00 < 16:00
         */
        assertThat(result).isTrue();
    }


    @Test
    void existsInterprovincialTripInShift_shouldReturnFalse_whenDepartureIsOutsideShift() {

        boolean result =
                tripRepository.existsInterprovincialTripInShift(
                        9001,
                        LocalTime.of(12, 0),
                        LocalTime.of(16, 0)
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // findByBranch_IdAndTripstatus_Name
    // ============================================================

    @Test
    void findByBranchAndStatus_shouldReturnMatchingTrips() {

        List<Trip> trips =
                tripRepository.findByBranch_IdAndTripstatus_Name(
                        9001,
                        "Active"
                );

        assertThat(trips)
                .extracting(Trip::getId)
                .containsExactlyInAnyOrder(9001, 9002, 9003);
    }


    @Test
    void findByBranchAndStatus_shouldReturnEmpty_whenStatusDoesNotMatch() {

        List<Trip> trips =
                tripRepository.findByBranch_IdAndTripstatus_Name(
                        9001,
                        "Completed"
                );

        assertThat(trips).isEmpty();
    }


    // ============================================================
    // existsByBranchIdAndTripstatus_Name
    // ============================================================

    @Test
    void existsByBranchIdAndStatus_shouldReturnTrue_whenMatchingTripExists() {

        boolean result =
                tripRepository.existsByBranchIdAndTripstatus_Name(
                        9001,
                        "Active"
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByBranchIdAndStatus_shouldReturnFalse_whenStatusDoesNotExist() {

        boolean result =
                tripRepository.existsByBranchIdAndTripstatus_Name(
                        9001,
                        "Completed"
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsActiveTrip
    // ============================================================

    @Test
    void existsActiveTrip_shouldReturnTrue_whenMatchingActiveTripExists() {

        boolean result =
                tripRepository.existsActiveTrip(
                        9001,
                        1,
                        LocalTime.of(8, 0),
                        null
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsActiveTrip_shouldReturnFalse_whenDepartureDoesNotMatch() {

        boolean result =
                tripRepository.existsActiveTrip(
                        9001,
                        1,
                        LocalTime.of(9, 0),
                        null
                );

        assertThat(result).isFalse();
    }


    @Test
    void existsActiveTrip_shouldReturnFalse_whenTripIsNotActive() {

        /*
         * Trip 9004 is Draft, not Active.
         */
        boolean result =
                tripRepository.existsActiveTrip(
                        9001,
                        1,
                        LocalTime.of(13, 0),
                        null
                );

        assertThat(result).isFalse();
    }


    @Test
    void existsActiveTrip_shouldExcludeSpecifiedTrip_whenTripIdIsProvided() {

        boolean result =
                tripRepository.existsActiveTrip(
                        9001,
                        1,
                        LocalTime.of(8, 0),
                        9001
                );

        /*
         * Trip 9001 matches all conditions,
         * but tripId = 9001 is explicitly excluded.
         */
        assertThat(result).isFalse();
    }
}
