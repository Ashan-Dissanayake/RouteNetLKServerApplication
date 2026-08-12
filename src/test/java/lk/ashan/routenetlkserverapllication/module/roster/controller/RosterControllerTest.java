package lk.ashan.routenetlkserverapllication.module.roster.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterService;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RosterController.class)
@Import(TestSecurityConfiguration.class)
class RosterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RosterService rosterService;

    private static final String API_URL = "/rosters";


    // =========================================================
    // GET /rosters/summaries
    // =========================================================

    @Test
    void getRosterSummary_shouldReturn200_whenAuthenticated() throws Exception {

        RosterSummaryDto response = new RosterSummaryDto(
                1,
                "Roster - Week 1"
        );

        when(rosterService.getRosterSummary())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL + "/summaries")
                        .with(user("test-user")))
                .andExpect(status().isOk());

        verify(rosterService).getRosterSummary();
    }

    @Test
    void getRosterSummary_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL + "/summaries"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(rosterService);
    }


    // =========================================================
    // POST /rosters
    // =========================================================

    @Test
    void createRoster_shouldReturn201_whenRequestIsValid() throws Exception {

        RosterRequestDto dto = validRosterRequest();

        RosterSummaryDto response =
                new RosterSummaryDto(
                        1,
                        "Roster - Week 1"
                );

        when(rosterService.createRoster(any(RosterRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "roster-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(rosterService)
                .createRoster(any(RosterRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createRoster_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<RosterRequestDto> mutator
    ) throws Exception {

        RosterRequestDto dto = validRosterRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "roster-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(
                        "$.details",
                        hasItem(field + ": " + errorMessage)
                ));

        verifyNoInteractions(rosterService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<RosterRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "dostartofweek",
                        "Start date is mandatory",
                        (Consumer<RosterRequestDto>)
                                dto -> dto.setDostartofweek(null)
                ),

                Arguments.of(
                        "doendofweek",
                        "End date is mandatory",
                        (Consumer<RosterRequestDto>)
                                dto -> dto.setDoendofweek(null)
                )
        );
    }


    @Test
    void createRoster_shouldReturn400_whenStartDateIsNotInFuture() throws Exception {

        RosterRequestDto dto = validRosterRequest();

        dto.setDostartofweek(LocalDate.now());

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "roster-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(
                        "$.details",
                        hasItem(containsString("Start date must be in the future"))
                ));

        verifyNoInteractions(rosterService);
    }


    @Test
    void createRoster_shouldReturn400_whenStartDateIsInThePast() throws Exception {

        RosterRequestDto dto = validRosterRequest();

        dto.setDostartofweek(LocalDate.now().minusDays(1));

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "roster-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(
                        "$.details",
                        hasItem(containsString("Start date must be in the future"))
                ));

        verifyNoInteractions(rosterService);
    }


    @Test
    void createRoster_shouldReturn403_whenWrongAuthority() throws Exception {

        RosterRequestDto dto = validRosterRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "roster-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(rosterService);
    }


    @Test
    void createRoster_shouldReturn401_whenAnonymous() throws Exception {

        RosterRequestDto dto = validRosterRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(rosterService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private RosterRequestDto validRosterRequest() {

        return new RosterRequestDto(
                BranchSummaryDto.builder()
                        .id(1)
                        .name("Colombo")
                        .build(),

                LocalDate.now().plusDays(7),

                LocalDate.now().plusDays(13)
        );
    }
}
