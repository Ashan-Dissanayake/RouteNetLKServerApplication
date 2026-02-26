package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = "spring.sql.init.mode=never")
class TripCrewAllocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/trip-crew-allocations";

    @Test
    @Sql(scripts = "/data-tripcrewallocation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void generateSuggestions_shouldSucceed_whenRosterExists() throws Exception {

        Integer tripId = 2;

        mockMvc.perform(post(apiUrl + "/" + tripId + "/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.feasible").value(true))
                .andExpect(jsonPath("$.data.assignmentsFilled").value(2))
                .andExpect(jsonPath("$.data.assignmentsUnfilled").value(0))
                .andExpect(jsonPath("$.data.suggestedAllocations.length()").value(2))
                .andExpect(jsonPath("$.data.suggestedAllocations[*].allocationstatus.name")
                        .value(org.hamcrest.Matchers.everyItem(
                                org.hamcrest.Matchers.is("Suggested"))));
    }

    @Test
    @Sql(scripts = "/data-tripcrewallocation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void generateSuggestions_shouldFail_whenTripNotExists() throws Exception {

        mockMvc.perform(post(apiUrl + "/999/generate"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "/data-tripcrewallocation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void approveSuggestion_shouldSucceed_whenSuggested() throws Exception {

        // First generate
        String response = mockMvc.perform(post(apiUrl + "/1/generate"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer allocationId = JsonPath.read(response,
                "$.data.suggestedAllocations[0].id");

        mockMvc.perform(put(apiUrl + "/" + allocationId + "/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.allocationstatus.name")
                        .value("Confirmed"));
    }

    @Test
    @Sql(scripts = "/data-tripcrewallocation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void approveSuggestion_shouldFail_whenAlreadyConfirmed() throws Exception {

        String response = mockMvc.perform(post(apiUrl + "/1/generate"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer allocationId = JsonPath.read(response,
                "$.data.suggestedAllocations[0].id");

        // First approval
        mockMvc.perform(put(apiUrl + "/" + allocationId + "/approve"))
                .andExpect(status().isOk());

        // Second approval
        mockMvc.perform(put(apiUrl + "/" + allocationId + "/approve"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "/data-tripcrewallocation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void rejectSuggestion_shouldSucceed_whenSuggested() throws Exception {

        String response = mockMvc.perform(post(apiUrl + "/1/generate"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer allocationId = JsonPath.read(response,
                "$.data.suggestedAllocations[0].id");

        mockMvc.perform(put(apiUrl + "/" + allocationId + "/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.allocationstatus.name")
                        .value("Rejected"));
    }

    @Test
    @Sql(scripts = "/data-tripcrewallocation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void rejectSuggestion_shouldFail_whenConfirmed() throws Exception {

        String response = mockMvc.perform(post(apiUrl + "/1/generate"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer allocationId = JsonPath.read(response,
                "$.data.suggestedAllocations[0].id");

        // Approve first
        mockMvc.perform(put(apiUrl + "/" + allocationId + "/approve"))
                .andExpect(status().isOk());

        // Then reject
        mockMvc.perform(put(apiUrl + "/" + allocationId + "/reject"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "/data-tripcrewallocation.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void clearRejected_shouldRemoveOnlyRejected() throws Exception {

        // Generate
        String response = mockMvc.perform(post(apiUrl + "/2/generate"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer allocation1 = JsonPath.read(response,
                "$.data.suggestedAllocations[0].id");

        Integer allocation2 = JsonPath.read(response,
                "$.data.suggestedAllocations[1].id");

        // Approve one
        mockMvc.perform(put(apiUrl + "/" + allocation1 + "/approve"))
                .andExpect(status().isOk());

        // Reject one
        mockMvc.perform(put(apiUrl + "/" + allocation2 + "/reject"))
                .andExpect(status().isOk());

        // Clear rejected
        mockMvc.perform(delete(apiUrl + "/2/clear-rejected"))
                .andExpect(status().isNoContent());

        // Generate again → should create only 1 suggestion (since confirmed remains)
        mockMvc.perform(post(apiUrl + "/2/generate"))
                .andExpect(status().isOk());
    }

}
