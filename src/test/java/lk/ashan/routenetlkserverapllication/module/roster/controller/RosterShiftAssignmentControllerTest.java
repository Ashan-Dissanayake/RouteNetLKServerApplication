package lk.ashan.routenetlkserverapllication.module.roster.controller;


import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterGenerationResponse;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterShiftAssignmentService;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RosterShiftAssignmentController.class)
@Import(TestSecurityConfiguration.class)
class RosterShiftAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RosterShiftAssignmentService rosterShiftAssignmentService;

    private static final String API_URL = "/roster-shift-assignment";


    // =========================================================
    // GET /roster-shift-assignment/view/{rosterId}
    // =========================================================

    @Test
    void getFullRoster_shouldReturn200_whenAuthorized() throws Exception {

        RosterShiftAssignmentResponseDto response =
                RosterShiftAssignmentResponseDto.builder()
                        .id(1)
                        .rosterName("Roster 001")
                        .shiftName("Morning")
                        .employeeName("Sunil")
                        .employeeNumber("EMP001")
                        .designation("Driver")
                        .shiftDate(LocalDate.now())
                        .startTime(LocalTime.of(5, 30))
                        .endTime(LocalTime.of(13, 30))
                        .status("PLANNED")
                        .build();

        when(rosterShiftAssignmentService.getAssignmentsByRosterId(1))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL + "/view/1")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-view")))
                .andExpect(status().isOk());

        verify(rosterShiftAssignmentService)
                .getAssignmentsByRosterId(1);
    }


    @Test
    void getFullRoster_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL + "/view/1")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-generate")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(rosterShiftAssignmentService);
    }


    @Test
    void getFullRoster_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL + "/view/1"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(rosterShiftAssignmentService);
    }


    // =========================================================
    // POST /roster-shift-assignment/{rosterId}/generate
    // =========================================================

    @Test
    void generateRoster_shouldReturn200_whenAuthorized() throws Exception {

        doNothing().when(rosterShiftAssignmentService)
                .generateRosterShiftAssignments(1);

        mockMvc.perform(post(API_URL + "/1/generate")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-generate")))
                .andExpect(status().isOk());

        verify(rosterShiftAssignmentService)
                .generateRosterShiftAssignments(1);
    }


    @Test
    void generateRoster_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/generate")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(rosterShiftAssignmentService);
    }


    @Test
    void generateRoster_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/generate"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(rosterShiftAssignmentService);
    }


    // =========================================================
    // POST /roster-shift-assignment/{assignmentId}/approved
    // =========================================================

    @Test
    void approveSuggestion_shouldReturn200_whenAuthorized() throws Exception {

        doNothing().when(rosterShiftAssignmentService)
                .approveSuggestion(1);

        mockMvc.perform(post(API_URL + "/1/approved")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-approved")))
                .andExpect(status().isOk());

        verify(rosterShiftAssignmentService)
                .approveSuggestion(1);
    }


    @Test
    void approveSuggestion_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/approved")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(rosterShiftAssignmentService);
    }


    @Test
    void approveSuggestion_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/approved"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(rosterShiftAssignmentService);
    }


    // =========================================================
    // POST /roster-shift-assignment/{assignmentId}/cancelled
    // =========================================================

    @Test
    void cancelSuggestion_shouldReturn200_whenAuthorized() throws Exception {

        doNothing().when(rosterShiftAssignmentService)
                .cancelSuggestion(1);

        mockMvc.perform(post(API_URL + "/1/cancelled")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-cancelled")))
                .andExpect(status().isOk());

        verify(rosterShiftAssignmentService)
                .cancelSuggestion(1);
    }


    @Test
    void cancelSuggestion_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/cancelled")
                        .with(user("test-user")
                                .authorities(() -> "roster-shift-assignment-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(rosterShiftAssignmentService);
    }


    @Test
    void cancelSuggestion_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/cancelled"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(rosterShiftAssignmentService);
    }
}
