package lk.ashan.routenetlkserverapllication.module.crew.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.crew.service.ConductorService;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConductorController.class)
@Import(TestSecurityConfiguration.class)
class ConductorControllerTest {

    private static final String API_URL = "/conductors";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ConductorService conductorService;


    // ==================== GET /conductors ====================

    @Test
    void getConductors_ShouldReturn200_WhenAuthorized() throws Exception {

        when(conductorService.getConductors()).thenReturn(List.of());
        mockMvc.perform(
                        get(API_URL)
                                .with(authority("conductor-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(conductorService).getConductors();
    }

    @Test
    void getConductors_ShouldReturn403_WhenWrongAuthority()
            throws Exception {

        mockMvc.perform(
                        get(API_URL)
                                .with(authority("conductor-add"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(conductorService);
    }

    @Test
    void getConductors_ShouldReturn401_WhenAnonymous()
            throws Exception {

        mockMvc.perform(
                        get(API_URL)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(conductorService);
    }

    @Test
    void getConductors_ShouldSearch_WhenQueryParamsProvided()
            throws Exception {

        when(conductorService.searchConductor(any()))
                .thenReturn(List.of());

        mockMvc.perform(
                        get(API_URL)
                                .param("name", "Sunil")
                                .with(authority("conductor-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(conductorService).searchConductor(
                argThat(params ->
                        "Sunil".equals(params.get("name"))
                )
        );

        verify(conductorService, never()).getConductors();
    }


    // ==================== POST /conductors ====================

    @Test
    void createConductor_ShouldReturn201_WhenRequestIsValid()
            throws Exception {

        ConductorCreateRequestDto request = validCreateRequest();

        when(conductorService.createConductor(
                any(ConductorCreateRequestDto.class)
        )).thenReturn(createResponse(request));

        mockMvc.perform(
                        post(API_URL)
                                .with(authority("conductor-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isCreated());

        verify(conductorService).createConductor(
                any(ConductorCreateRequestDto.class)
        );
    }

    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createConductor_ShouldReturn400_WhenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<ConductorCreateRequestDto> mutator
    ) throws Exception {

        ConductorCreateRequestDto request = validCreateRequest();

        mutator.accept(request);

        mockMvc.perform(
                        post(API_URL)
                                .with(authority("conductor-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                field + ": " + errorMessage
                        )));

        verifyNoInteractions(conductorService);
    }

    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(
                Arguments.of(
                        "domedicalissued",
                        "Medical issued date is mandatory",
                        (Consumer<ConductorCreateRequestDto>)
                                dto -> dto.setDomedicalissued(null)
                ),

                Arguments.of(
                        "domedicalexpired",
                        "Medical expired date is mandatory",
                        (Consumer<ConductorCreateRequestDto>)
                                dto -> dto.setDomedicalexpired(null)
                ),

                Arguments.of(
                        "crewstatus",
                        "Crew status is mandatory",
                        (Consumer<ConductorCreateRequestDto>)
                                dto -> dto.setCrewstatus(null)
                ),

                Arguments.of(
                        "routefamiliaritylevel",
                        "Route Familiarity Level is mandatory",
                        (Consumer<ConductorCreateRequestDto>)
                                dto -> dto.setRoutefamiliaritylevel(null)
                ),

                Arguments.of(
                        "employee",
                        "Employee is mandatory",
                        (Consumer<ConductorCreateRequestDto>)
                                dto -> dto.setEmployee(null)
                )
        );
    }

    @Test
    void createConductor_ShouldReturn403_WhenWrongAuthority()
            throws Exception {

        ConductorCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post(API_URL)
                                .with(authority("conductor-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(conductorService);
    }

    @Test
    void createConductor_ShouldReturn401_WhenAnonymous()
            throws Exception {

        ConductorCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(conductorService);
    }


    // ==================== PUT /conductors ====================

    @Test
    void updateConductor_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        ConductorUpdateRequestDto request = validUpdateRequest();

        when(conductorService.updateConductor(
                any(ConductorUpdateRequestDto.class)
        )).thenReturn(updateResponse(request));

        mockMvc.perform(
                        put(API_URL)
                                .with(authority("conductor-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk());

        verify(conductorService).updateConductor(
                any(ConductorUpdateRequestDto.class)
        );
    }

    @ParameterizedTest
    @MethodSource("missingUpdateFieldProvider")
    void updateConductor_ShouldReturn400_WhenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<ConductorUpdateRequestDto> mutator
    ) throws Exception {

        ConductorUpdateRequestDto request = validUpdateRequest();

        mutator.accept(request);

        mockMvc.perform(
                        put(API_URL)
                                .with(authority("conductor-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                field + ": " + errorMessage
                        )));

        verifyNoInteractions(conductorService);
    }

    static Stream<Arguments> missingUpdateFieldProvider() {

        return Stream.of(
                Arguments.of(
                        "domedicalissued",
                        "Medical issued date is mandatory",
                        (Consumer<ConductorUpdateRequestDto>)
                                dto -> dto.setDomedicalissued(null)
                ),

                Arguments.of(
                        "domedicalexpired",
                        "Medical expired date is mandatory",
                        (Consumer<ConductorUpdateRequestDto>)
                                dto -> dto.setDomedicalexpired(null)
                ),

                Arguments.of(
                        "crewstatus",
                        "Crew status is mandatory",
                        (Consumer<ConductorUpdateRequestDto>)
                                dto -> dto.setCrewstatus(null)
                ),

                Arguments.of(
                        "routefamiliaritylevel",
                        "Route Familiarity Level is mandatory",
                        (Consumer<ConductorUpdateRequestDto>)
                                dto -> dto.setRoutefamiliaritylevel(null)
                ),

                Arguments.of(
                        "employee",
                        "Employee is mandatory",
                        (Consumer<ConductorUpdateRequestDto>)
                                dto -> dto.setEmployee(null)
                )
        );
    }

    @Test
    void updateConductor_ShouldReturn400_WhenIdIsMissing()
            throws Exception {

        ConductorUpdateRequestDto request = validUpdateRequest();
        request.setId(null);

        mockMvc.perform(
                        put(API_URL)
                                .with(authority("conductor-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(conductorService);
    }

    @Test
    void updateConductor_ShouldReturn403_WhenWrongAuthority()
            throws Exception {

        ConductorUpdateRequestDto request = validUpdateRequest();

        mockMvc.perform(
                        put(API_URL)
                                .with(authority("conductor-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(conductorService);
    }

    @Test
    void updateConductor_ShouldReturn401_WhenAnonymous()
            throws Exception {

        ConductorUpdateRequestDto request = validUpdateRequest();

        mockMvc.perform(
                        put(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(conductorService);
    }


    // ==================== Test Data ====================

    private ConductorCreateRequestDto validCreateRequest() {

        return ConductorCreateRequestDto.builder()
                .employee(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .callingname("Sunil")
                                .build()
                )
                .domedicalissued(
                        LocalDate.now().minusMonths(1)
                )
                .domedicalexpired(
                        LocalDate.now().plusMonths(5)
                )
                .routefamiliaritylevel(
                        RouteFamiliarityLevelDto.builder()
                                .id(1)
                                .name("Low")
                                .build()
                )
                .crewstatus(
                        CrewStatusDto.builder()
                                .id(1)
                                .name("Eligible")
                                .build()
                )
                .build();
    }

    private ConductorUpdateRequestDto validUpdateRequest() {

        return ConductorUpdateRequestDto.builder()
                .id(1)
                .employee(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .callingname("Sunil")
                                .build()
                )
                .domedicalissued(
                        LocalDate.now().minusMonths(1)
                )
                .domedicalexpired(
                        LocalDate.now().plusMonths(5)
                )
                .routefamiliaritylevel(
                        RouteFamiliarityLevelDto.builder()
                                .id(1)
                                .name("Low")
                                .build()
                )
                .crewstatus(
                        CrewStatusDto.builder()
                                .id(1)
                                .name("Eligible")
                                .build()
                )
                .build();
    }


    // ==================== Response Data ====================

    private ConductorDetailResponseDto createResponse(
            ConductorCreateRequestDto request) {

        return ConductorDetailResponseDto.builder()
                .id(1)
                .number("CON0001")
                .domedicalissued(request.getDomedicalissued())
                .domedicalexpired(request.getDomedicalexpired())
                .crewstatus(request.getCrewstatus())
                .routefamiliaritylevel(request.getRoutefamiliaritylevel())
                .employee(request.getEmployee())
                .build();
    }

    private ConductorDetailResponseDto updateResponse(
            ConductorUpdateRequestDto request) {

        return ConductorDetailResponseDto.builder()
                .id(request.getId())
                .number("CON0001")
                .domedicalissued(request.getDomedicalissued())
                .domedicalexpired(request.getDomedicalexpired())
                .crewstatus(request.getCrewstatus())
                .routefamiliaritylevel(request.getRoutefamiliaritylevel())
                .employee(request.getEmployee())
                .build();
    }


    // ==================== Helpers ====================

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
