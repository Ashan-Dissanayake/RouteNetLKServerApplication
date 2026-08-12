package lk.ashan.routenetlkserverapllication.module.incident.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentStatusDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentTypeDto;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentService;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
@Import(TestSecurityConfiguration.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IncidentService incidentService;

    private static final String API_URL = "/incidents";


    // =========================================================
    // GET /incidents
    // =========================================================

    @Test
    void getIncidents_shouldReturn200_whenAuthorized() throws Exception {

        IncidentDetailResponseDto response = validResponse();

        when(incidentService.getIncidents())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "incident-view")))
                .andExpect(status().isOk());

        verify(incidentService).getIncidents();
    }


    @Test
    void getIncidents_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "incident-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(incidentService);
    }


    @Test
    void getIncidents_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    @Test
    void searchIncidents_shouldCallSearchService_whenQueryParamsProvided()
            throws Exception {

        IncidentDetailResponseDto response = validResponse();

        when(incidentService.searchIncidents(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("remarks", "Breakdown")
                        .with(user("test-user")
                                .authorities(() -> "incident-view")))
                .andExpect(status().isOk());

        verify(incidentService).searchIncidents(any());
        verify(incidentService, never()).getIncidents();
    }


    // =========================================================
    // GET /incidents/summaries
    // =========================================================

    @Test
    void getIncidentSummaries_shouldReturn200_whenAuthenticated()
            throws Exception {

        IncidentSummaryDto response = IncidentSummaryDto.builder()
                .id(1)
                .build();

        when(incidentService.getSummaryIncidents())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL + "/summaries")
                        .with(user("test-user")))
                .andExpect(status().isOk());

        verify(incidentService).getSummaryIncidents();
    }


    @Test
    void getIncidentSummaries_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(get(API_URL + "/summaries"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    // =========================================================
    // POST /incidents
    // =========================================================

    @Test
    void createIncident_shouldReturn201_whenRequestIsValid()
            throws Exception {

        IncidentCreateRequestDto dto = validCreateRequest();

        IncidentDetailResponseDto response = validResponse();

        when(incidentService.create(any(IncidentCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "incident-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(incidentService).create(
                any(IncidentCreateRequestDto.class)
        );
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createIncident_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<IncidentCreateRequestDto> mutator
    ) throws Exception {

        IncidentCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "incident-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(incidentService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "tripexecution",
                        "Trip Execution is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setTripexecution(null)
                ),

                Arguments.of(
                        "incidenttype",
                        "Incident type is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setIncidenttype(null)
                ),

                Arguments.of(
                        "regionalarea",
                        "Regional area is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setRegionalarea(null)
                ),

                Arguments.of(
                        "toreported",
                        "Time is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setToreported(null)
                ),

                Arguments.of(
                        "doreported",
                        "Date is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setDoreported(null)
                ),

                Arguments.of(
                        "remarks",
                        "Description is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setRemarks(null)
                ),

                Arguments.of(
                        "incidentstatus",
                        "Status is mandatory",
                        (Consumer<IncidentCreateRequestDto>)
                                dto -> dto.setIncidentstatus(null)
                )
        );
    }


    @Test
    void createIncident_shouldReturn403_whenWrongAuthority()
            throws Exception {

        IncidentCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "incident-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(incidentService);
    }


    @Test
    void createIncident_shouldReturn401_whenAnonymous()
            throws Exception {

        IncidentCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    // =========================================================
    // POST /incidents/{id}/in-progress
    // =========================================================

    @Test
    void inProgress_shouldReturn200_whenAuthorized()
            throws Exception {

        IncidentDetailResponseDto response = validResponse();

        when(incidentService.inProgress(1))
                .thenReturn(response);

        mockMvc.perform(post(API_URL + "/1/in-progress")
                        .with(user("test-user")
                                .authorities(() -> "incident-start")))
                .andExpect(status().isOk());

        verify(incidentService).inProgress(1);
    }


    @Test
    void inProgress_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/in-progress")
                        .with(user("test-user")
                                .authorities(() -> "incident-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(incidentService);
    }


    @Test
    void inProgress_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/in-progress"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    // =========================================================
    // POST /incidents/{id}/vehicle-recovery
    // =========================================================

    @Test
    void vehicleRecovery_shouldReturn200_whenAuthorized()
            throws Exception {

        when(incidentService.vehicleRecovery(1))
                .thenReturn(validResponse());

        mockMvc.perform(post(API_URL + "/1/vehicle-recovery")
                        .with(user("test-user")
                                .authorities(() -> "incident-vehicle-recovery")))
                .andExpect(status().isOk());

        verify(incidentService).vehicleRecovery(1);
    }


    @Test
    void vehicleRecovery_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/vehicle-recovery")
                        .with(user("test-user")
                                .authorities(() -> "incident-start")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(incidentService);
    }


    @Test
    void vehicleRecovery_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/vehicle-recovery"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    // =========================================================
    // POST /incidents/{id}/pending-allocation
    // =========================================================

    @Test
    void pendingAllocation_shouldReturn200_whenAuthorized()
            throws Exception {

        when(incidentService.pendingAllocation(1))
                .thenReturn(validResponse());

        mockMvc.perform(post(API_URL + "/1/pending-allocation")
                        .with(user("test-user")
                                .authorities(() -> "incident-pending-allocation")))
                .andExpect(status().isOk());

        verify(incidentService).pendingAllocation(1);
    }


    @Test
    void pendingAllocation_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/pending-allocation")
                        .with(user("test-user")
                                .authorities(() -> "incident-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(incidentService);
    }


    @Test
    void pendingAllocation_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/pending-allocation"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    // =========================================================
    // POST /incidents/{id}/resolved
    // =========================================================

    @Test
    void resolved_shouldReturn200_whenAuthorized()
            throws Exception {

        when(incidentService.resolved(1))
                .thenReturn(validResponse());

        mockMvc.perform(post(API_URL + "/1/resolved")
                        .with(user("test-user")
                                .authorities(() -> "incident-resolve")))
                .andExpect(status().isOk());

        verify(incidentService).resolved(1);
    }


    @Test
    void resolved_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/resolved")
                        .with(user("test-user")
                                .authorities(() -> "incident-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(incidentService);
    }


    @Test
    void resolved_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/resolved"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    // =========================================================
    // POST /incidents/{id}/closed
    // =========================================================

    @Test
    void closed_shouldReturn200_whenAuthorized()
            throws Exception {

        when(incidentService.closed(1))
                .thenReturn(validResponse());

        mockMvc.perform(post(API_URL + "/1/closed")
                        .with(user("test-user")
                                .authorities(() -> "incident-close")))
                .andExpect(status().isOk());

        verify(incidentService).closed(1);
    }


    @Test
    void closed_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/closed")
                        .with(user("test-user")
                                .authorities(() -> "incident-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(incidentService);
    }


    @Test
    void closed_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(post(API_URL + "/1/closed"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(incidentService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private IncidentCreateRequestDto validCreateRequest() {

        return IncidentCreateRequestDto.builder()
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .tripexecution(
                        TripExecutionSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .incidenttype(
                        IncidentTypeDto.builder()
                                .id(1)
                                .build()
                )
                .regionalarea(
                        RegionalOffice.builder()
                                .id(1)
                                .build()
                )
                .toreported(LocalTime.of(10, 30))
                .doreported(LocalDate.now())
                .remarks("Vehicle breakdown")
                .incidentstatus(
                        IncidentStatusDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }


    private IncidentDetailResponseDto validResponse() {

        return IncidentDetailResponseDto.builder()
                .id(1)
                .toreported(LocalTime.of(10, 30))
                .doreported(LocalDate.now())
                .remarks("Vehicle breakdown")
                .build();
    }
}
