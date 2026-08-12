package lk.ashan.routenetlkserverapllication.module.grn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnPartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.service.GrnService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GrnController.class)
@Import(TestSecurityConfiguration.class)
class GrnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GrnService grnService;

    private static final String API_URL = "/grns";

    // =========================================================
    // GET /grns
    // =========================================================

    @Test
    void getGrns_shouldReturn200_whenAuthorized() throws Exception {

        GrnDetailResponseDto response =
                GrnDetailResponseDto.builder()
                        .id(1)
                        .number("GRN001")
                        .doreceived(LocalDate.now().minusDays(2))
                        .remarks("Received successfully")
                        .build();

        when(grnService.getGrns())
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "grn-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(1));

        verify(grnService).getGrns();
    }

    @Test
    void getGrns_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(
                        get(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "grn-update"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(grnService);
    }

    @Test
    void getGrns_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(
                        get(API_URL)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(grnService);
    }

    @Test
    void searchGrns_shouldCallSearchService_whenQueryParamsProvided()
            throws Exception {

        GrnDetailResponseDto response =
                GrnDetailResponseDto.builder()
                        .id(1)
                        .number("GRN001")
                        .build();

        when(grnService.searchGrns(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get(API_URL)
                                .param("number", "GRN001")
                                .with(user("test-user")
                                        .authorities(() -> "grn-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(1));

        verify(grnService).searchGrns(any());
        verify(grnService, never()).getGrns();
    }

    // =========================================================
    // PUT /grns
    // =========================================================

    @Test
    void updateGrn_shouldReturn200_whenRequestIsValid()
            throws Exception {

        GrnUpdateRequestDto dto = validUpdateRequest();

        GrnDetailResponseDto response =
                GrnDetailResponseDto.builder()
                        .id(1)
                        .number("GRN001")
                        .doreceived(dto.getDoreceived())
                        .remarks(dto.getRemarks())
                        .grnpartrequestitems(dto.getGrnpartrequestitems())
                        .build();

        when(grnService.updateGrn(any(GrnUpdateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(
                        put(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "grn-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.meta.status").value("updated"));

        verify(grnService).updateGrn(
                any(GrnUpdateRequestDto.class)
        );
    }

    @ParameterizedTest
    @MethodSource("missingUpdateFieldProvider")
    void updateGrn_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<GrnUpdateRequestDto> mutator
    ) throws Exception {

        GrnUpdateRequestDto dto = validUpdateRequest();

        mutator.accept(dto);

        mockMvc.perform(
                        put(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "grn-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(
                                hasItem(field + ": " + errorMessage)
                        ));

        verifyNoInteractions(grnService);
    }

    static Stream<Arguments> missingUpdateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "id",
                        "GRN ID is mandatory",
                        (Consumer<GrnUpdateRequestDto>)
                                dto -> dto.setId(null)
                ),

                Arguments.of(
                        "doreceived",
                        "Received is mandatory",
                        (Consumer<GrnUpdateRequestDto>)
                                dto -> dto.setDoreceived(null)
                ),

                Arguments.of(
                        "grnpartrequestitems",
                        "GRN parts are mandatory",
                        (Consumer<GrnUpdateRequestDto>)
                                dto -> dto.setGrnpartrequestitems(null)
                )
        );
    }

    @Test
    void updateGrn_shouldReturn403_whenWrongAuthority()
            throws Exception {

        GrnUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(
                        put(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "grn-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(grnService);
    }

    @Test
    void updateGrn_shouldReturn401_whenAnonymous()
            throws Exception {

        GrnUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(
                        put(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(grnService);
    }

    // =========================================================
    // Test Data
    // =========================================================

    private GrnUpdateRequestDto validUpdateRequest() {

        return GrnUpdateRequestDto.builder()
                .id(1)
                .doreceived(LocalDate.now().minusDays(1))
                .remarks("Received in good condition")
                .grnpartrequestitems(List.of(
                        GrnPartRequestItemDto.builder()
                                .build()
                ))
                .build();
    }
}
