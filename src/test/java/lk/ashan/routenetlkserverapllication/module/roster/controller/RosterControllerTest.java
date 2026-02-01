package lk.ashan.routenetlkserverapllication.module.roster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.config.factory.RosterDtoFactory;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterConfirmationRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RosterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/rosters";
    @Autowired
    private RosterRepository rosterRepository;

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createRoster_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<RosterCreateRequestDto> mutator) throws Exception {
        RosterCreateRequestDto dto = RosterDtoFactory.createUniqueRosterCreateRequest();
        mutator.accept(dto);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        field + ": " + errorMessage
                ));
    }

    static Stream<Arguments> missingFieldProvider() {
        return Stream.of(
                Arguments.of("doroster", "Roster date can not be empty", (Consumer<RosterCreateRequestDto>) dto -> dto.setDoroster(null)),
                Arguments.of("shift", "Shift can not be empty", (Consumer<RosterCreateRequestDto>) dto -> dto.setShift(null)),
                Arguments.of("branch", "Branch can not be empty", (Consumer<RosterCreateRequestDto>) dto -> dto.setBranch(null)),
                Arguments.of("rosterstatus", "Roster Status can not be empty", (Consumer<RosterCreateRequestDto>) dto -> dto.setRosterstatus(null))
        );
    }

    @Test
    void createRoster_shouldFail_whenRosterDateIsNotFuture() throws Exception{
        RosterCreateRequestDto dto = RosterDtoFactory.createUniqueRosterCreateRequest();
        dto.setDoroster(LocalDate.now().minusMonths(2));


        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "doroster: Roster date can not be past"
                ));
    }

    @Test
    void createRoster_shouldFail_whenRosterAlreadyExistsForBranchAndDate() throws Exception{
        RosterCreateRequestDto dto = RosterDtoFactory.createUniqueRosterCreateRequest();

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Roster already existed"
                ));
    }

    @Test
    void solveRoster_shouldFail_whenRosterNotFound() throws Exception{

        Integer rosterId = 8;

        mockMvc.perform(post(apiUrl+"/{id}/solve",rosterId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Roster not found"
                ));
    }

    @Test
    void confirmRoster_shouldFail_whenRosterNotFound() throws Exception {

        RosterConfirmationRequestDto request = RosterDtoFactory.CreateConfirmationRequestDto(
                2,
                LocalDate.of(2026,1,12)
                ,true
        );

        mockMvc.perform(post(apiUrl+"/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "No SOLVED roster found for given branch and date"
                ));
    }

}
