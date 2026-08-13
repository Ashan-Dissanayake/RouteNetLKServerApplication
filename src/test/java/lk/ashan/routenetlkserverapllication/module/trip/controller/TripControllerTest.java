package lk.ashan.routenetlkserverapllication.module.trip.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.ShiftSummaryDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OpCalenderSummaryDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OriginTerminalDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripService;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripController.class)
@Import(TestSecurityConfiguration.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TripService tripService;

    private static final String API_URL = "/trips";


    // =========================================================
    // GET /trips
    // =========================================================

    @Test
    void getTrips_shouldReturn200_whenAuthorized() throws Exception {

        TripDetailResponseDto response = validResponse();

        when(tripService.getTrips())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "trip-view")))
                .andExpect(status().isOk());

        verify(tripService).getTrips();
    }


    @Test
    void getTrips_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "trip-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripService);
    }


    @Test
    void getTrips_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripService);
    }


    @Test
    void searchTrips_shouldCallSearchService_whenQueryParamsProvided() throws Exception {

        TripDetailResponseDto response = validResponse();

        when(tripService.searchTrips(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("tripstatus", "ACTIVE")
                        .with(user("test-user")
                                .authorities(() -> "trip-view")))
                .andExpect(status().isOk());

        verify(tripService).searchTrips(any());
        verify(tripService, never()).getTrips();
    }


    // =========================================================
    // POST /trips
    // =========================================================

    @Test
    void createTrip_shouldReturn201_whenRequestIsValid() throws Exception {

        TripCreateRequestDto dto = validCreateRequest();

        TripDetailResponseDto response = validResponse();

        when(tripService.createTrip(any(TripCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "trip-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(tripService).createTrip(any(TripCreateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createTrip_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<TripCreateRequestDto> mutator
    ) throws Exception {

        TripCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "trip-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(tripService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "triptype",
                        "Trip type is mandatory",
                        (Consumer<TripCreateRequestDto>)
                                dto -> dto.setTriptype(null)
                ),

                Arguments.of(
                        "permite",
                        "Permit is mandatory",
                        (Consumer<TripCreateRequestDto>)
                                dto -> dto.setPermite(null)
                ),

                Arguments.of(
                        "todepature",
                        "Departure time is mandatory",
                        (Consumer<TripCreateRequestDto>)
                                dto -> dto.setTodepature(null)
                ),

                Arguments.of(
                        "toarrival",
                        "Arrival time is mandatory",
                        (Consumer<TripCreateRequestDto>)
                                dto -> dto.setToarrival(null)
                ),

                Arguments.of(
                        "shift",
                        "Shift is Mandatory",
                        (Consumer<TripCreateRequestDto>)
                                dto -> dto.setShift(null)
                ),

                Arguments.of(
                        "originterminal",
                        "Origin terminal is mandatory",
                        (Consumer<TripCreateRequestDto>)
                                dto -> dto.setOriginterminal(null)
                ),

                Arguments.of(
                        "opcalender",
                        "OP Calender is mandatory",
                        (Consumer<TripCreateRequestDto>)
                                dto -> dto.setOpcalender(null)
                )
        );
    }


    @Test
    void createTrip_shouldReturn403_whenWrongAuthority() throws Exception {

        TripCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "trip-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripService);
    }


    @Test
    void createTrip_shouldReturn401_whenAnonymous() throws Exception {

        TripCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripService);
    }


    // =========================================================
    // POST /trips/{tripId}/activate-trip
    // =========================================================

    @Test
    void activateTrip_shouldReturn200_whenAuthorized() throws Exception {

        TripDetailResponseDto response = validResponse();

        when(tripService.activateTrip(1))
                .thenReturn(response);

        mockMvc.perform(post(API_URL + "/1/activate-trip")
                        .with(user("test-user")
                                .authorities(() -> "trip-activate")))
                .andExpect(status().isOk());

        verify(tripService).activateTrip(1);
    }


    @Test
    void activateTrip_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/activate-trip")
                        .with(user("test-user")
                                .authorities(() -> "trip-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripService);
    }


    @Test
    void activateTrip_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/activate-trip"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripService);
    }


    // =========================================================
    // POST /trips/{tripId}/suspend-trip
    // =========================================================

    @Test
    void suspendTrip_shouldReturn200_whenAuthorized() throws Exception {

        TripDetailResponseDto response = validResponse();

        when(tripService.suspendTrip(1))
                .thenReturn(response);

        mockMvc.perform(post(API_URL + "/1/suspend-trip")
                        .with(user("test-user")
                                .authorities(() -> "trip-suspend")))
                .andExpect(status().isOk());

        verify(tripService).suspendTrip(1);
    }


    @Test
    void suspendTrip_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/suspend-trip")
                        .with(user("test-user")
                                .authorities(() -> "trip-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripService);
    }


    @Test
    void suspendTrip_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/suspend-trip"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripService);
    }


    // =========================================================
    // POST /trips/{tripId}/discontinue-trip
    // =========================================================

    @Test
    void discontinueTrip_shouldReturn200_whenAuthorized() throws Exception {

        TripDetailResponseDto response = validResponse();

        when(tripService.discontinueTrip(1))
                .thenReturn(response);

        mockMvc.perform(post(API_URL + "/1/discontinue-trip")
                        .with(user("test-user")
                                .authorities(() -> "trip-discontinue")))
                .andExpect(status().isOk());

        verify(tripService).discontinueTrip(1);
    }


    @Test
    void discontinueTrip_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/discontinue-trip")
                        .with(user("test-user")
                                .authorities(() -> "trip-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripService);
    }


    @Test
    void discontinueTrip_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/discontinue-trip"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private TripCreateRequestDto validCreateRequest() {

        return TripCreateRequestDto.builder()
                .triptype(
                        TripTypeDto.builder()
                                .id(1)
                                .name("Regular")
                                .build()
                )
                .permite(
                        PermitSummaryRequestDto.builder()
                                .id(1)
                                .number("A12345")
                                .build()
                )
                .todepature(LocalTime.of(8, 0))
                .toarrival(LocalTime.of(10, 0))
                .remarks("Morning trip")
                .tripstatus(
                        TripStatusDto.builder()
                                .id(1)
                                .name("ACTIVE")
                                .build()
                )
                .shift(validShift())
                .originterminal(
                        OriginTerminalDto.builder()
                                .id(1)
                                .name("Colombo")
                                .build()
                )
                .opcalender(
                        OpCalenderSummaryDto.builder()
                                .id(1)
                                .name("Weekday")
                                .build()
                )
                .build();
    }

    private ShiftSummaryDto validShift() {
        ShiftSummaryDto shift = new ShiftSummaryDto();
        shift.setId(1);
        shift.setName("Morning Shift");
        shift.setTostart(LocalTime.of(6, 0));
        shift.setToend(LocalTime.of(14, 0));
        shift.setShiftFullName("Morning Shift (06:00 - 14:00)");
        return shift;
    }

    private TripDetailResponseDto validResponse() {

        return TripDetailResponseDto.builder()
                .id(1)
                .triptype(
                        TripTypeDto.builder()
                                .id(1)
                                .name("Regular")
                                .build()
                )
                .todepature(LocalTime.of(8, 0))
                .toarrival(LocalTime.of(10, 0))
                .tripstatus(
                        TripStatusDto.builder()
                                .id(1)
                                .name("ACTIVE")
                                .build()
                )
                .build();
    }

}
