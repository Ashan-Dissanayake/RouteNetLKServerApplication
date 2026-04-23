package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripAllocationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/modules/tripcrewallocation/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/tripcrewallocation/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TripCrewAllocationControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/trip-crew-allocations";


    @Test
    void allocateManual_shouldSucceedWithExactDbStructure() throws Exception {
        // Using IDs from the data.sql above (Trip 50, RSA 101)
        TripAllocationRequest request = new TripAllocationRequest(50, 101, "Manual allocation test");

        mockMvc.perform(post(API_URL + "/allocate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tripId").value(50))
                .andExpect(jsonPath("$.data.rostershiftassignmentId").value(101))
                .andExpect(jsonPath("$.data.statusName").value("ALLOCATED"));
    }

}
