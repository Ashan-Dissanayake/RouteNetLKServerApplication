package lk.ashan.routenetlkserverapllication.module.roster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.DayOfWeek;
import java.time.LocalDate;



import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.mockito.AdditionalMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = "spring.sql.init.mode=never")
class RosterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_URL = "/rosters";


    // Create Roster with Valid Week Dates

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createRoster_shouldSucceed_whenWeekDatesValid() throws Exception {
        // Given: Valid week (Monday to Sunday)
        LocalDate monday = getNextMonday();
        LocalDate sunday = monday.plusDays(6);


        RosterCreateRequestDto dto = RosterCreateRequestDto.builder()
                .branch(createBranchDto(1, "Colombo Head Office"))
                .dostartofweek(monday)
                .doendofweek(sunday)
                .build();

        // When: Creating the roster
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.rosterstatus.name").value("Draft"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createRoster_shouldFail_whenNotStartingMonday() throws Exception {
        // Given: Week starting on Wednesday
        LocalDate wednesday = LocalDate.now().plusDays(1).with(DayOfWeek.SATURDAY);
        LocalDate tuesday = wednesday.plusDays(6);

        RosterCreateRequestDto dto = RosterCreateRequestDto.builder()
                .branch(createBranchDto(1, "Colombo Head Office"))
                .dostartofweek(wednesday)
                .doendofweek(tuesday)
                .build();

        // When: Creating the roster
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // Then: Request rejected with validation error
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value("Roster week must start on Monday. Given date is: SATURDAY"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createRoster_shouldFail_whenNotSevenDays() throws Exception {
        // Given: Week duration = 5 days
        LocalDate monday = getNextMonday();
        LocalDate friday = monday.plusDays(4); // Only 5 days

        RosterCreateRequestDto dto = RosterCreateRequestDto.builder()
                .branch(createBranchDto(1, "Colombo Head Office"))
                .dostartofweek(monday)
                .doendofweek(friday)
                .build();

        // When: Creating the roster
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // Then: Request rejected
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value("Roster must cover exactly one week (7 days). Current range: 5 days"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createRoster_shouldFail_whenOverlapping() throws Exception {
        // Given: First roster already exists in test data (Roster ID 1)
        // Roster 1: Branch 1, Week: 2026-03-02 to 2026-03-08 (from SQL file)

        LocalDate existingMonday = LocalDate.parse("2026-03-02");
        LocalDate existingSunday = LocalDate.parse("2026-03-08");

        // When: Attempting to create overlapping roster (same week, same branch)
        RosterCreateRequestDto dto = RosterCreateRequestDto.builder()
                .branch(createBranchDto(1, "Colombo Head Office"))
                .dostartofweek(existingMonday)  // Same week as Roster 1
                .doendofweek(existingSunday)
                .build();

        // Then: Request rejected due to overlap
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value("Cannot create roster more than 1 weeks in advance"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateRoster_shouldSucceed_whenDraft() throws Exception {
        // Given: Roster in DRAFT status (assume ID 1 from test data)
        Integer rosterId = 1;
        LocalDate newMonday = getNextMonday().plusDays(7); // Next week
        LocalDate newSunday = newMonday.plusDays(6);

        RosterUpdateRequestDto dto = RosterUpdateRequestDto.builder()
                .branch(createBranchDto(1, "Colombo Head Office"))
                .id(rosterId)
                .dostartofweek(newMonday)
                .doendofweek(newSunday)
                .build();

        // When: Updating roster
        mockMvc.perform(put(API_URL + "/{id}", rosterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // Then: Update successful
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(rosterId))
                .andExpect(jsonPath("$.data.rosterstatus.name").value("Draft"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateRoster_shouldFail_whenLocked() throws Exception {
        // Given: Roster in LOCKED status (assume ID 2 from test data)
        Integer rosterId = 2;
        LocalDate newMonday = getNextMonday().plusDays(7);
        LocalDate newSunday = newMonday.plusDays(6);

        RosterUpdateRequestDto dto = RosterUpdateRequestDto.builder()
                .id(rosterId)
                .branch(createBranchDto(1, "Colombo Head Office"))
                .dostartofweek(newMonday)
                .doendofweek(newSunday)
                .build();

        // When: Attempting to update
        mockMvc.perform(put(API_URL + "/{id}", rosterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // Then: Update blocked
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value("Cannot update roster. Only DRAFT rosters can be edited. Current status: Locked"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void generateSuggestions_shouldSucceed_whenDraftAndDataAvailable() throws Exception {
        // Given: Roster in DRAFT with sufficient employees
        Integer rosterId = 1;

        // When: Generating suggestions
        mockMvc.perform(post(API_URL + "/{id}/generate-suggestions", rosterId))
                // Then: Suggestions created successfully
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rosterId").value(rosterId))
                .andExpect(jsonPath("$.data.feasible").value(true))
                .andExpect(jsonPath("$.data.assignmentsFilled").exists())
                .andExpect(jsonPath("$.data.score", containsString("0hard")));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void generateSuggestions_shouldFail_whenInsufficientEmployees() throws Exception {
        // Given: Roster for branch with very few employees (assume ID 5)
        Integer rosterId = 5;

        // When: Attempting to generate suggestions
        mockMvc.perform(post(API_URL + "/{id}/generate-suggestions", rosterId))
                // Then: Infeasible solution reported
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details", containsString("feasible")));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void uapproveSuggestion_shouldSucceed_whenSuggestedAndDraft() throws Exception {
        // Given: Assignment in SUGGESTED status (assume ID 101)
        Integer assignmentId = 1;

        // When: Approving assignment
        mockMvc.perform(post(API_URL + "/assignments/{id}/approve", assignmentId))
                // Then: Approval successful
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(assignmentId));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void rejectSuggestion_shouldSucceed_whenSuggestedAndDraft() throws Exception {
        // Given: Assignment in SUGGESTED status (assume ID 102)
        Integer assignmentId = 2;

        // When: Rejecting assignment
        mockMvc.perform(delete(API_URL + "/assignments/{id}/reject", assignmentId))
                // Then: Rejection successful, assignment deleted
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void lockRoster_shouldSucceed_whenDraftWithAssignments() throws Exception {
        // Given: Roster in DRAFT with assignments (assume ID 1)
        Integer rosterId = 1;

        // When: Locking roster
        mockMvc.perform(post(API_URL + "/{id}/lock", rosterId))
                // Then: Status changes to LOCKED
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(rosterId))
                .andExpect(jsonPath("$.data.rosterstatus.name").value("Locked"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void lockRoster_shouldFail_whenNoAssignments() throws Exception {
        // Given: Roster in DRAFT without assignments (assume ID 3)
        Integer rosterId = 3;

        // When: Attempting to lock
        mockMvc.perform(post(API_URL + "/{id}/lock", rosterId))
                // Then: Lock blocked
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value("Cannot lock roster without any shift assignments"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void unlockRoster_shouldSucceed_andResetConfirmations() throws Exception {
        // Given: Roster in LOCKED status (assume ID 2)
        Integer rosterId = 2;

        // When: Unlocking roster
        mockMvc.perform(post(API_URL + "/{id}/unlock", rosterId))
                // Then: Status changes to DRAFT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(rosterId))
                .andExpect(jsonPath("$.data.rosterstatus.name").value("Draft"));

        // And: CONFIRMED assignments reset to SUGGESTED (verify by querying)
        mockMvc.perform(get(API_URL + "/{id}/suggestions", rosterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", not(empty())));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void archiveRoster_shouldSucceed_whenAllConfirmed() throws Exception {
        // Given: Roster in LOCKED with all CONFIRMED (assume ID 4)
        Integer rosterId = 4;

        // When: Archiving roster
        mockMvc.perform(post(API_URL + "/{id}/archive", rosterId))
                // Then: Status changes to ARCHIVED
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(rosterId))
                .andExpect(jsonPath("$.data.rosterstatus.name").value("Archived"));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void archiveRoster_shouldFail_whenUnconfirmed() throws Exception {
        // Given: Roster in LOCKED with some SUGGESTED (assume ID 2)
        Integer rosterId = 2;

        // When: Attempting to archive
        mockMvc.perform(post(API_URL + "/{id}/archive", rosterId))
                // Then: Archive blocked
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value("Cannot archive roster with 1 unconfirmed assignment(s). All assignments must be confirmed first."));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void regenerateSuggestions_shouldSucceed_clearOldAndGenerateNew() throws Exception {
        // Given: Roster with existing suggestions (assume ID 1)
        Integer rosterId = 1;

        // When: Regenerating suggestions
        mockMvc.perform(post(API_URL + "/{id}/regenerate-suggestions", rosterId))
                // Then: New suggestions created
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rosterId").value(rosterId))
                .andExpect(jsonPath("$.data.feasible").value(true));
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void clearAllSuggestions_shouldSucceed() throws Exception {
        // Given: Roster with suggestions (assume ID 1)
        Integer rosterId = 1;

        // When: Clearing all suggestions
        mockMvc.perform(delete(API_URL + "/{id}/suggestions", rosterId))
                // Then: All SUGGESTED assignments deleted
                .andExpect(status().isOk());

        // Verify: No suggestions remain
        mockMvc.perform(get(API_URL + "/{id}/suggestions", rosterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", empty()));
    }

//    @Test
//    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    void deleteRoster_shouldSucceed_whenDraft() throws Exception {
//        // Given: Roster in DRAFT (assume ID 6)
//        Integer rosterId = 6;
//
//        // When: Deleting roster
//        mockMvc.perform(delete(API_URL + "/{id}", rosterId))
//                // Then: Soft deleted successfully
//                .andExpect(status().isOk());
//
//        // Verify: Roster still in DB but deleted=true
//        mockMvc.perform(get(API_URL + "/{id}", rosterId))
//                .andExpect(status().isNotFound());
//    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteRoster_shouldFail_whenLocked() throws Exception {
        // Given: Roster in LOCKED (assume ID 2)
        Integer rosterId = 2;

        // When: Attempting to delete
        mockMvc.perform(delete(API_URL + "/{id}", rosterId))
                // Then: Delete blocked
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value("Cannot delete roster. Only DRAFT rosters can be deleted. Current status: Locked"));
    }

    private LocalDate getNextMonday() {
        LocalDate today = LocalDate.now();
//        return today.plusDays((7 - today.getDayOfWeek().getValue() + 1) % 7 + 1);
        return today.plusDays((7 - today.getDayOfWeek().getValue() + 1) % 7);
    }

    private BranchSummaryResponseDto createBranchDto(Integer id, String name) {
        return BranchSummaryResponseDto.builder()
                .id(id)
                .name(name)
                .build();
    }

}
