package lk.ashan.routenetlkserverapllication.module.farecollection.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.TicketMachineDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.service.FareCollectionService;
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

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FareCollectionController.class)
@Import(TestSecurityConfiguration.class)
class FareCollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FareCollectionService fareCollectionService;

    private static final String API_URL = "/fare-collections";

    // =========================================================
    // GET /fare-collections
    // =========================================================

    @Test
    void getFareCollections_shouldReturn200_whenAuthorized() throws Exception {

        FareCollectionDetailResponseDto response =
                FareCollectionDetailResponseDto.builder()
                        .id(1)
                        .totaltickets(100)
                        .cachecollected(BigDecimal.valueOf(5000))
                        .digitalpayments(BigDecimal.valueOf(2500))
                        .isreconciled(false)
                        .tocollected(LocalTime.of(18, 30))
                        .build();

        when(fareCollectionService.getFareCollections())
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "fare-collection-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(1));

        verify(fareCollectionService).getFareCollections();
    }

    @Test
    void getFareCollections_shouldReturn403_whenWrongAuthority()
            throws Exception {

        mockMvc.perform(
                        get(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "fare-collection-add"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(fareCollectionService);
    }

    @Test
    void getFareCollections_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(
                        get(API_URL)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(fareCollectionService);
    }

    @Test
    void searchFareCollections_shouldCallSearchService_whenQueryParamsProvided()
            throws Exception {

        FareCollectionDetailResponseDto response =
                FareCollectionDetailResponseDto.builder()
                        .id(1)
                        .totaltickets(100)
                        .build();

        when(fareCollectionService.searchFareCollections(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get(API_URL)
                                .param("branch", "1")
                                .with(user("test-user")
                                        .authorities(() -> "fare-collection-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(1));

        verify(fareCollectionService).searchFareCollections(any());
        verify(fareCollectionService, never()).getFareCollections();
    }

    // =========================================================
    // POST /fare-collections
    // =========================================================

    @Test
    void createFareCollection_shouldReturn201_whenRequestIsValid()
            throws Exception {

        FareCollectionCreateRequestDto dto = validCreateRequest();

        FareCollectionDetailResponseDto response =
                FareCollectionDetailResponseDto.builder()
                        .id(1)
                        .branch(dto.getBranch())
                        .tripexecution(dto.getTripexecution())
                        .ticketmachine(dto.getTicketmachine())
                        .totaltickets(dto.getTotaltickets())
                        .cachecollected(dto.getCachecollected())
                        .digitalpayments(dto.getDigitalpayments())
                        .isreconciled(false)
                        .tocollected(LocalTime.of(18, 30))
                        .build();

        when(fareCollectionService.createFareCollection(
                any(FareCollectionCreateRequestDto.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "fare-collection-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.meta.status").value("created"));

        verify(fareCollectionService).createFareCollection(
                any(FareCollectionCreateRequestDto.class)
        );
    }

    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createFareCollection_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<FareCollectionCreateRequestDto> mutator
    ) throws Exception {

        FareCollectionCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "fare-collection-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(fareCollectionService);
    }

    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<FareCollectionCreateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "tripexecution",
                        "Trip Execution is mandatory",
                        (Consumer<FareCollectionCreateRequestDto>)
                                dto -> dto.setTripexecution(null)
                ),

                Arguments.of(
                        "ticketmachine",
                        "Ticket Machine is mandatory",
                        (Consumer<FareCollectionCreateRequestDto>)
                                dto -> dto.setTicketmachine(null)
                ),

                Arguments.of(
                        "totaltickets",
                        "Total Ticket Amount is mandatory",
                        (Consumer<FareCollectionCreateRequestDto>)
                                dto -> dto.setTotaltickets(null)
                ),

                Arguments.of(
                        "cachecollected",
                        "Cache Collected Amount is mandatory",
                        (Consumer<FareCollectionCreateRequestDto>)
                                dto -> dto.setCachecollected(null)
                ),

                Arguments.of(
                        "digitalpayments",
                        "Digital Payment Amount is mandatory",
                        (Consumer<FareCollectionCreateRequestDto>)
                                dto -> dto.setDigitalpayments(null)
                )
        );
    }

    @Test
    void createFareCollection_shouldReturn403_whenWrongAuthority()
            throws Exception {

        FareCollectionCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "fare-collection-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(fareCollectionService);
    }

    @Test
    void createFareCollection_shouldReturn401_whenAnonymous()
            throws Exception {

        FareCollectionCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(
                        post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(fareCollectionService);
    }

    // =========================================================
    // POST /fare-collections/{fareCollectionId}/reconciled
    // =========================================================

    @Test
    void reconcileFareCollection_shouldReturn200_whenAuthorized()
            throws Exception {

        Integer fareCollectionId = 1;

        doNothing().when(fareCollectionService)
                .reconciled(fareCollectionId);

        mockMvc.perform(
                        post(API_URL + "/{fareCollectionId}/reconciled",
                                fareCollectionId)
                                .with(user("test-user")
                                        .authorities(
                                                () -> "fare-collection-reconcile"
                                        ))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Reconciled for 1"))
                .andExpect(jsonPath("$.status")
                        .value("SUCCESS"));

        verify(fareCollectionService).reconciled(fareCollectionId);
    }

    @Test
    void reconcileFareCollection_shouldReturn403_whenWrongAuthority()
            throws Exception {

        Integer fareCollectionId = 1;

        mockMvc.perform(
                        post(API_URL + "/{fareCollectionId}/reconciled",
                                fareCollectionId)
                                .with(user("test-user")
                                        .authorities(
                                                () -> "fare-collection-view"
                                        ))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(fareCollectionService);
    }

    @Test
    void reconcileFareCollection_shouldReturn401_whenAnonymous()
            throws Exception {

        Integer fareCollectionId = 1;

        mockMvc.perform(
                        post(API_URL + "/{fareCollectionId}/reconciled",
                                fareCollectionId)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(fareCollectionService);
    }

    // =========================================================
    // Test Data
    // =========================================================

    private FareCollectionCreateRequestDto validCreateRequest() {

        return FareCollectionCreateRequestDto.builder()
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .name("Dambulla")
                                .build()
                )
                .tripexecution(
                        TripExecutionSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .ticketmachine(
                        TicketMachineDto.builder()
                                .id(1)
                                .build()
                )
                .totaltickets(100)
                .cachecollected(BigDecimal.valueOf(5000))
                .digitalpayments(BigDecimal.valueOf(2500))
                .build();
    }
}
