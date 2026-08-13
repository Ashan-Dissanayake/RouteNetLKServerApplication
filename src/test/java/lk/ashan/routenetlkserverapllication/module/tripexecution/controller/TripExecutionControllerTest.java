package lk.ashan.routenetlkserverapllication.module.tripexecution.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionInitializationDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionService;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TripExecutionController.class)
@Import(TestSecurityConfiguration.class)
class TripExecutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TripExecutionService tripExecutionService;

    @Autowired
    private ObjectMapper objectMapper;


    // ==================== GET /trip-execution ====================

    @Test
    void getTripExecutions_ShouldReturn200_WhenUserHasViewAuthority()
            throws Exception {

        when(tripExecutionService.getTripExecutions())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/trip-execution")
                                .with(authority("trip-execution-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(tripExecutionService).getTripExecutions();
    }


    @Test
    void getTripExecutions_ShouldReturn403_WhenUserLacksViewAuthority()
            throws Exception {

        mockMvc.perform(
                        get("/trip-execution")
                                .with(authority("vehicle-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void getTripExecutions_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        get("/trip-execution")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void searchTripExecutions_ShouldReturn200_WhenParametersAreProvided()
            throws Exception {

        when(tripExecutionService.searchTripExecutions(any()))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/trip-execution")
                                .param("status", "PLANNED")
                                .param("date", "2026-08-13")
                                .with(authority("trip-execution-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(tripExecutionService)
                .searchTripExecutions(any());
    }


    // ==================== GET /trip-execution/summaries ====================

    @Test
    void getSummaries_ShouldReturn200_WhenAuthenticated()
            throws Exception {

        when(tripExecutionService.getSummaryTripExecution())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/trip-execution/summaries")
                                .with(authenticatedUser())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(tripExecutionService).getSummaryTripExecution();
    }


    @Test
    void getSummaries_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        get("/trip-execution/summaries")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/initialize ====================

    @Test
    void initializeTripExecutions_ShouldReturn201_WhenUserHasInitializeAuthority()
            throws Exception {

        TripExecutionInitializationDto request =
                validInitializationRequest();

        when(tripExecutionService.initializeDailyExecutions(
                any(TripExecutionInitializationDto.class)))
                .thenReturn(List.of());

        mockMvc.perform(
                        post("/trip-execution/initialize")
                                .with(authority("trip-execution-initialize"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.status").value("created"));

        verify(tripExecutionService)
                .initializeDailyExecutions(
                        any(TripExecutionInitializationDto.class)
                );
    }


    @Test
    void initializeTripExecutions_ShouldReturn403_WhenUserLacksInitializeAuthority()
            throws Exception {

        TripExecutionInitializationDto request =
                validInitializationRequest();

        mockMvc.perform(
                        post("/trip-execution/initialize")
                                .with(authority("trip-execution-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void initializeTripExecutions_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        TripExecutionInitializationDto request =
                validInitializationRequest();

        mockMvc.perform(
                        post("/trip-execution/initialize")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/generate-assignments ====================

    @Test
    void generateAssignments_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        TripExecutionAssignmentDto request =
                validAssignmentRequest();

        doNothing().when(tripExecutionService)
                .generateTripExecutionAssignments(
                        any(TripExecutionAssignmentDto.class)
                );

        mockMvc.perform(
                        post("/trip-execution/generate-assignments")
                                .with(authority(
                                        "trip-execution-generate-assignments"
                                ))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Optimization completed successfully for 2026-08-13"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(tripExecutionService)
                .generateTripExecutionAssignments(
                        any(TripExecutionAssignmentDto.class)
                );
    }


    @Test
    void generateAssignments_ShouldReturn403_WhenUserLacksAuthority()
            throws Exception {

        TripExecutionAssignmentDto request =
                validAssignmentRequest();

        mockMvc.perform(
                        post("/trip-execution/generate-assignments")
                                .with(authority("trip-execution-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void generateAssignments_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        TripExecutionAssignmentDto request =
                validAssignmentRequest();

        mockMvc.perform(
                        post("/trip-execution/generate-assignments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/{id}/checked-in ====================

    @Test
    void checkedInTripExecution_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        Integer tripExecutionId = 1;

        doNothing().when(tripExecutionService)
                .checkedInTripExecution(tripExecutionId);

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/checked-in",
                                tripExecutionId)
                                .with(authority("trip-execution-checked-in"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Checked-In for 1"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(tripExecutionService)
                .checkedInTripExecution(tripExecutionId);
    }


    @Test
    void checkedInTripExecution_ShouldReturn403_WhenUserLacksAuthority()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/checked-in", 1)
                                .with(authority("trip-execution-view"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void checkedInTripExecution_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/checked-in", 1)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/{id}/dispatched ====================

    @Test
    void dispatchedTripExecution_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        Integer tripExecutionId = 1;

        doNothing().when(tripExecutionService)
                .dispatchedTripExecution(tripExecutionId);

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/dispatched",
                                tripExecutionId)
                                .with(authority("trip-execution-dispatched"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Dispatched for 1"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(tripExecutionService)
                .dispatchedTripExecution(tripExecutionId);
    }


    @Test
    void dispatchedTripExecution_ShouldReturn403_WhenUserLacksAuthority()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/dispatched", 1)
                                .with(authority("trip-execution-view"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void dispatchedTripExecution_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/dispatched", 1)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/{id}/arrived ====================

    @Test
    void arrivedTripExecution_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        Integer tripExecutionId = 1;

        doNothing().when(tripExecutionService)
                .arrivedTripExecution(tripExecutionId);

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/arrived",
                                tripExecutionId)
                                .with(authority("trip-execution-arrived"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Arrived for 1"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(tripExecutionService)
                .arrivedTripExecution(tripExecutionId);
    }


    @Test
    void arrivedTripExecution_ShouldReturn403_WhenUserLacksAuthority()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/arrived", 1)
                                .with(authority("trip-execution-view"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void arrivedTripExecution_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/arrived", 1)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/{id}/breakdown ====================

    @Test
    void breakdownTripExecution_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        Integer tripExecutionId = 1;

        doNothing().when(tripExecutionService)
                .breakdownTripExecution(tripExecutionId);

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/breakdown",
                                tripExecutionId)
                                .with(authority("trip-execution-breakdown"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Breakdown for 1"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(tripExecutionService)
                .breakdownTripExecution(tripExecutionId);
    }


    @Test
    void breakdownTripExecution_ShouldReturn403_WhenUserLacksAuthority()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/breakdown", 1)
                                .with(authority("trip-execution-view"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void breakdownTripExecution_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/breakdown", 1)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/{id}/completed ====================

    @Test
    void completedTripExecution_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        Integer tripExecutionId = 1;

        doNothing().when(tripExecutionService)
                .completedTripExecution(tripExecutionId);

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/completed",
                                tripExecutionId)
                                .with(authority("trip-execution-completed"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Completed for 1"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(tripExecutionService)
                .completedTripExecution(tripExecutionId);
    }


    @Test
    void completedTripExecution_ShouldReturn403_WhenUserLacksAuthority()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/completed", 1)
                                .with(authority("trip-execution-view"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void completedTripExecution_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/completed", 1)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== POST /trip-execution/{id}/cancelled ====================

    @Test
    void cancelledTripExecution_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        Integer tripExecutionId = 1;

        doNothing().when(tripExecutionService)
                .cancelledTripExecution(tripExecutionId);

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/cancelled",
                                tripExecutionId)
                                .with(authority("trip-execution-cancelled"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Cancelled for 1"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(tripExecutionService)
                .cancelledTripExecution(tripExecutionId);
    }


    @Test
    void cancelledTripExecution_ShouldReturn403_WhenUserLacksAuthority()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/cancelled", 1)
                                .with(authority("trip-execution-view"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(tripExecutionService);
    }


    @Test
    void cancelledTripExecution_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        post("/trip-execution/{tripExecutionId}/cancelled", 1)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tripExecutionService);
    }


    // ==================== Test Data ====================

    private TripExecutionInitializationDto validInitializationRequest() {

        Branch branch = new Branch();
        branch.setId(1);

        return TripExecutionInitializationDto.builder()
                .branch(branch)
                .doservice(LocalDate.of(2026, 8, 13))
                .build();
    }


    private TripExecutionAssignmentDto validAssignmentRequest() {

        return TripExecutionAssignmentDto.builder()
                .branchId(1)
                .date(LocalDate.of(2026, 8, 13))
                .build();
    }


    // ==================== Helpers ====================

    private RequestPostProcessor authenticatedUser() {
        return user("testuser");
    }

    private RequestPostProcessor authority(String authority) {
        return user("testuser")
                .authorities(
                        new SimpleGrantedAuthority(authority)
                );
    }

    private String json(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
