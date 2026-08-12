package lk.ashan.routenetlkserverapllication.module.sparepart.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartMasterDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.service.PartService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
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

@WebMvcTest(PartController.class)
@Import(TestSecurityConfiguration.class)
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PartService partService;

    private static final String API_URL = "/parts";


    // =========================================================
    // GET /parts
    // =========================================================

    @Test
    void getParts_shouldReturn200_whenAuthorized() throws Exception {

        PartDetailResponseDto response = validResponse();

        when(partService.getParts())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-view")))
                .andExpect(status().isOk());

        verify(partService).getParts();
    }


    @Test
    void getParts_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partService);
    }


    @Test
    void getParts_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partService);
    }


    @Test
    void searchParts_shouldCallSearchService_whenQueryParamsProvided() throws Exception {

        PartDetailResponseDto response = validResponse();

        when(partService.searchParts(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("remarks", "Brake")
                        .with(user("test-user")
                                .authorities(() -> "part-view")))
                .andExpect(status().isOk());

        verify(partService).searchParts(any());
        verify(partService, never()).getParts();
    }


    // =========================================================
    // GET /parts/summaries
    // =========================================================

    @Test
    void getPartSummaries_shouldReturn200_whenAuthenticated() throws Exception {

        PartSummaryDto response =
                PartSummaryDto.builder()
                        .id(1)
                        .name("Brake Pad")
                        .build();

        when(partService.getSummaryParts())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL + "/summaries")
                        .with(user("test-user")))
                .andExpect(status().isOk());

        verify(partService).getSummaryParts();
    }


    @Test
    void getPartSummaries_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL + "/summaries"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partService);
    }


    // =========================================================
    // POST /parts
    // =========================================================

    @Test
    void createPart_shouldReturn201_whenRequestIsValid() throws Exception {

        PartCreateRequestDto dto = validCreateRequest();

        PartDetailResponseDto response = validResponse();

        when(partService.createPart(any(PartCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(partService).createPart(any(PartCreateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createPart_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PartCreateRequestDto> mutator
    ) throws Exception {

        PartCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(partService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "branch",
                        "Branch is required",
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "qoh",
                        "QOH is required",
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setQoh(null)
                ),

                Arguments.of(
                        "maxlevel",
                        "Max level is required",
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setMaxlevel(null)
                ),

                Arguments.of(
                        "rop",
                        "ROP is required",
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setRop(null)
                ),

                Arguments.of(
                        "partstatus",
                        "Part status is required",
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setPartstatus(null)
                ),

                Arguments.of(
                        "partmaster",
                        "Part Master is required",
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setPartmaster(null)
                )
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCreateValueProvider")
    void createPart_shouldReturn400_whenValueIsInvalid(
            Consumer<PartCreateRequestDto> mutator,
            String errorMessage
    ) throws Exception {

        PartCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(errorMessage))));

        verifyNoInteractions(partService);
    }


    static Stream<Arguments> invalidCreateValueProvider() {

        return Stream.of(

                Arguments.of(
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setQoh(BigDecimal.valueOf(-1)),
                        "QOH cannot be negative"
                ),

                Arguments.of(
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setMaxlevel(BigDecimal.ZERO),
                        "Max level must be greater than 0"
                ),

                Arguments.of(
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setRop(BigDecimal.ZERO),
                        "ROP must be greater than 0"
                ),

                Arguments.of(
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setDolastordered(
                                        LocalDate.now().plusDays(1)
                                ),
                        "Date cannot be in the future"
                ),

                Arguments.of(
                        (Consumer<PartCreateRequestDto>)
                                dto -> dto.setRemarks("@@@"),
                        "Remarks contains invalid characters"
                )
        );
    }


    @Test
    void createPart_shouldReturn403_whenWrongAuthority() throws Exception {

        PartCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partService);
    }


    @Test
    void createPart_shouldReturn401_whenAnonymous() throws Exception {

        PartCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partService);
    }


    // =========================================================
    // PUT /parts
    // =========================================================

    @Test
    void updatePart_shouldReturn200_whenRequestIsValid() throws Exception {

        PartUpdateRequestDto dto = validUpdateRequest();

        PartDetailResponseDto response = validResponse();

        when(partService.updatePart(any(PartUpdateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(partService).updatePart(any(PartUpdateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingUpdateFieldProvider")
    void updatePart_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PartUpdateRequestDto> mutator
    ) throws Exception {

        PartUpdateRequestDto dto = validUpdateRequest();

        mutator.accept(dto);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(partService);
    }


    static Stream<Arguments> missingUpdateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "id",
                        "ID is required",
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setId(null)
                ),

                Arguments.of(
                        "branch",
                        "Branch is required",
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "qoh",
                        "QOH is required",
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setQoh(null)
                ),

                Arguments.of(
                        "maxlevel",
                        "Max level is required",
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setMaxlevel(null)
                ),

                Arguments.of(
                        "rop",
                        "ROP is required",
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setRop(null)
                ),

                Arguments.of(
                        "partstatus",
                        "Part status is required",
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setPartstatus(null)
                ),

                Arguments.of(
                        "partmaster",
                        "Part Master is required",
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setPartmaster(null)
                )
        );
    }


    @ParameterizedTest
    @MethodSource("invalidUpdateValueProvider")
    void updatePart_shouldReturn400_whenValueIsInvalid(
            Consumer<PartUpdateRequestDto> mutator,
            String errorMessage
    ) throws Exception {

        PartUpdateRequestDto dto = validUpdateRequest();

        mutator.accept(dto);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(errorMessage))));

        verifyNoInteractions(partService);
    }


    static Stream<Arguments> invalidUpdateValueProvider() {

        return Stream.of(

                Arguments.of(
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setQoh(BigDecimal.valueOf(-1)),
                        "QOH cannot be negative"
                ),

                Arguments.of(
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setMaxlevel(BigDecimal.ZERO),
                        "Max level must be greater than 0"
                ),

                Arguments.of(
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setRop(BigDecimal.ZERO),
                        "ROP must be greater than 0"
                ),

                Arguments.of(
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setDolastordered(
                                        LocalDate.now().plusDays(1)
                                ),
                        "Date cannot be in the future"
                ),

                Arguments.of(
                        (Consumer<PartUpdateRequestDto>)
                                dto -> dto.setRemarks("@@@"),
                        "Remarks contains invalid characters"
                )
        );
    }


    @Test
    void updatePart_shouldReturn403_whenWrongAuthority() throws Exception {

        PartUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partService);
    }


    @Test
    void updatePart_shouldReturn401_whenAnonymous() throws Exception {

        PartUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partService);
    }


    // =========================================================
    // POST /parts/deactivate
    // =========================================================

    @Test
    void deactivateParts_shouldReturn200_whenAuthorized() throws Exception {

        List<Integer> ids = List.of(1, 2);

        when(partService.deactivateParts(anyList()))
                .thenReturn(ids);

        mockMvc.perform(post(API_URL + "/deactivate")
                        .with(user("test-user")
                                .authorities(() -> "part-delete"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk());

        verify(partService).deactivateParts(anyList());
    }


    @Test
    void deactivateParts_shouldReturn403_whenWrongAuthority() throws Exception {

        List<Integer> ids = List.of(1, 2);

        mockMvc.perform(post(API_URL + "/deactivate")
                        .with(user("test-user")
                                .authorities(() -> "part-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partService);
    }


    @Test
    void deactivateParts_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1, 2))))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private PartCreateRequestDto validCreateRequest() {

        return PartCreateRequestDto.builder()
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .remarks("Brake Pad")
                .qoh(BigDecimal.valueOf(10))
                .maxlevel(BigDecimal.valueOf(100))
                .rop(BigDecimal.valueOf(20))
                .dolastordered(LocalDate.now().minusDays(10))
                .partstatus(
                        PartStatusDto.builder()
                                .id(1)
                                .name("ACTIVE")
                                .build()
                )
                .partmaster(
                        PartMasterDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }


    private PartUpdateRequestDto validUpdateRequest() {

        return PartUpdateRequestDto.builder()
                .id(1)
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .remarks("Brake Pad")
                .qoh(BigDecimal.valueOf(10))
                .maxlevel(BigDecimal.valueOf(100))
                .rop(BigDecimal.valueOf(20))
                .dolastordered(LocalDate.now().minusDays(10))
                .partstatus(
                        PartStatusDto.builder()
                                .id(1)
                                .name("ACTIVE")
                                .build()
                )
                .partmaster(
                        PartMasterDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }


    private PartDetailResponseDto validResponse() {

        return PartDetailResponseDto.builder()
                .id(1)
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .remarks("Brake Pad")
                .qoh(BigDecimal.valueOf(10))
                .maxlevel(BigDecimal.valueOf(100))
                .rop(BigDecimal.valueOf(20))
                .dolastordered(LocalDate.now().minusDays(10))
                .partstatus(
                        PartStatusDto.builder()
                                .id(1)
                                .name("ACTIVE")
                                .build()
                )
                .partmaster(
                        PartMasterDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }
}
