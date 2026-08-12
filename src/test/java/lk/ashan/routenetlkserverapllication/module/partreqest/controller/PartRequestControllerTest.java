package lk.ashan.routenetlkserverapllication.module.partreqest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.partreqest.service.PartRequestService;
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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
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

@WebMvcTest(PartRequestController.class)
@Import(TestSecurityConfiguration.class)
class PartRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PartRequestService partRequestService;

    private static final String API_URL = "/part-requests";


    // =========================================================
    // GET /part-requests
    // =========================================================

    @Test
    void getPartRequests_shouldReturn200_whenAuthorized() throws Exception {

        PartRequestDetailResponseDto response =
                PartRequestDetailResponseDto.builder()
                        .id(1)
                        .number("PR001")
                        .dorequested(LocalDate.now())
                        .build();

        when(partRequestService.getPartRequests())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-view")))
                .andExpect(status().isOk());

        verify(partRequestService).getPartRequests();
    }


    @Test
    void getPartRequests_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-add")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partRequestService);
    }


    @Test
    void getPartRequests_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partRequestService);
    }


    @Test
    void searchPartRequests_shouldCallSearchService_whenQueryParamsProvided() throws Exception {

        PartRequestDetailResponseDto response =
                PartRequestDetailResponseDto.builder()
                        .id(1)
                        .number("PR001")
                        .build();

        when(partRequestService.searchPartRequests(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("number", "PR001")
                        .with(user("test-user")
                                .authorities(() -> "part-request-view")))
                .andExpect(status().isOk());

        verify(partRequestService).searchPartRequests(any());
        verify(partRequestService, never()).getPartRequests();
    }


    // =========================================================
    // GET /part-requests/summaries
    // =========================================================

    @Test
    void getSummaryPartRequests_shouldReturn200_whenAuthenticated() throws Exception {

        PartRequestSummaryDto response =
                PartRequestSummaryDto.builder()
                        .id(1)
                        .build();

        when(partRequestService.getSummaryPartRequests())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL + "/summaries")
                        .with(user("test-user")))
                .andExpect(status().isOk());

        verify(partRequestService).getSummaryPartRequests();
    }


    @Test
    void getSummaryPartRequests_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL + "/summaries"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partRequestService);
    }


    // =========================================================
    // POST /part-requests
    // =========================================================

    @Test
    void createPartRequest_shouldReturn201_whenRequestIsValid() throws Exception {

        PartRequestCreateRequestDto dto = validCreateRequest();

        PartRequestDetailResponseDto response =
                PartRequestDetailResponseDto.builder()
                        .id(1)
                        .number("PR001")
                        .build();

        when(partRequestService.createRequest(any(PartRequestCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(partRequestService)
                .createRequest(any(PartRequestCreateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createPartRequest_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PartRequestCreateRequestDto> mutator
    ) throws Exception {

        PartRequestCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(partRequestService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<PartRequestCreateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "dorequested",
                        "Requested date is mandatory",
                        (Consumer<PartRequestCreateRequestDto>)
                                dto -> dto.setDorequested(null)
                ),

                Arguments.of(
                        "partrequeststatus",
                        "Status is mandatory",
                        (Consumer<PartRequestCreateRequestDto>)
                                dto -> dto.setPartrequeststatus(null)
                ),

                Arguments.of(
                        "partrequestitems",
                        "Request items are mandatory",
                        (Consumer<PartRequestCreateRequestDto>)
                                dto -> dto.setPartrequestitems(null)
                )
        );
    }


    @Test
    void createPartRequest_shouldReturn400_whenRequestedDateIsInFuture() throws Exception {

        PartRequestCreateRequestDto dto = validCreateRequest();

        dto.setDorequested(LocalDate.now().plusDays(1));

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(
                                "Requested date cannot be in the future"
                        ))));

        verifyNoInteractions(partRequestService);
    }


    @Test
    void createPartRequest_shouldReturn403_whenWrongAuthority() throws Exception {

        PartRequestCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partRequestService);
    }


    @Test
    void createPartRequest_shouldReturn401_whenAnonymous() throws Exception {

        PartRequestCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partRequestService);
    }


    // =========================================================
    // PUT /part-requests
    // =========================================================

    @Test
    void updatePartRequest_shouldReturn200_whenRequestIsValid() throws Exception {

        PartRequestUpdateRequestDto dto = validUpdateRequest();

        PartRequestDetailResponseDto response =
                PartRequestDetailResponseDto.builder()
                        .id(1)
                        .number("PR001")
                        .build();

        when(partRequestService.updateRequest(any(PartRequestUpdateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(partRequestService)
                .updateRequest(any(PartRequestUpdateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingUpdateFieldProvider")
    void updatePartRequest_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PartRequestUpdateRequestDto> mutator
    ) throws Exception {

        PartRequestUpdateRequestDto dto = validUpdateRequest();

        mutator.accept(dto);

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(partRequestService);
    }


    static Stream<Arguments> missingUpdateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "id",
                        "Id is mandatory",
                        (Consumer<PartRequestUpdateRequestDto>)
                                dto -> dto.setId(null)
                ),

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<PartRequestUpdateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "dorequested",
                        "Requested date is mandatory",
                        (Consumer<PartRequestUpdateRequestDto>)
                                dto -> dto.setDorequested(null)
                ),

                Arguments.of(
                        "partrequeststatus",
                        "Status is mandatory",
                        (Consumer<PartRequestUpdateRequestDto>)
                                dto -> dto.setPartrequeststatus(null)
                ),

                Arguments.of(
                        "partrequestitems",
                        "Request items are mandatory",
                        (Consumer<PartRequestUpdateRequestDto>)
                                dto -> dto.setPartrequestitems(null)
                )
        );
    }


    @Test
    void updatePartRequest_shouldReturn400_whenRequestedDateIsInFuture() throws Exception {

        PartRequestUpdateRequestDto dto = validUpdateRequest();

        dto.setDorequested(LocalDate.now().plusDays(1));

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-update"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString(
                                "Requested date cannot be in the future"
                        ))));

        verifyNoInteractions(partRequestService);
    }


    @Test
    void updatePartRequest_shouldReturn403_whenWrongAuthority() throws Exception {

        PartRequestUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(put(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "part-request-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partRequestService);
    }


    @Test
    void updatePartRequest_shouldReturn401_whenAnonymous() throws Exception {

        PartRequestUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partRequestService);
    }


    // =========================================================
    // POST /part-requests/{id}/approve
    // =========================================================

    @Test
    void approvePartRequest_shouldReturn200_whenAuthorized() throws Exception {

        PartRequestDetailResponseDto response =
                PartRequestDetailResponseDto.builder()
                        .id(1)
                        .number("PR001")
                        .build();

        when(partRequestService.approveRequest(1))
                .thenReturn(response);

        mockMvc.perform(post(API_URL + "/1/approve")
                        .with(user("test-user")
                                .authorities(() -> "part-request-approve")))
                .andExpect(status().isOk());

        verify(partRequestService).approveRequest(1);
    }


    @Test
    void approvePartRequest_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/approve")
                        .with(user("test-user")
                                .authorities(() -> "part-request-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partRequestService);
    }


    @Test
    void approvePartRequest_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/approve"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partRequestService);
    }


    // =========================================================
    // POST /part-requests/{id}/reject
    // =========================================================

    @Test
    void rejectPartRequest_shouldReturn200_whenAuthorized() throws Exception {

        PartRequestDetailResponseDto response =
                PartRequestDetailResponseDto.builder()
                        .id(1)
                        .number("PR001")
                        .build();

        when(partRequestService.rejectRequest(1))
                .thenReturn(response);

        mockMvc.perform(post(API_URL + "/1/reject")
                        .with(user("test-user")
                                .authorities(() -> "part-request-reject")))
                .andExpect(status().isOk());

        verify(partRequestService).rejectRequest(1);
    }


    @Test
    void rejectPartRequest_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(post(API_URL + "/1/reject")
                        .with(user("test-user")
                                .authorities(() -> "part-request-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(partRequestService);
    }


    @Test
    void rejectPartRequest_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(post(API_URL + "/1/reject"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(partRequestService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private PartRequestCreateRequestDto validCreateRequest() {

        return PartRequestCreateRequestDto.builder()
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .number("PR001")
                .dorequested(LocalDate.now())
                .remarks("Required spare parts")
                .partrequeststatus(
                        PartRequestStatusDto.builder()
                                .id(1)
                                .build()
                )
                .partrequestitems(List.of(
                        PartRequestItemDto.builder()
                                .build()
                ))
                .build();
    }


    private PartRequestUpdateRequestDto validUpdateRequest() {

        return PartRequestUpdateRequestDto.builder()
                .id(1)
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .number("PR001")
                .dorequested(LocalDate.now())
                .remarks("Updated spare parts")
                .partrequeststatus(
                        PartRequestStatusDto.builder()
                                .id(1)
                                .build()
                )
                .partrequestitems(List.of(
                        PartRequestItemDto.builder()
                                .build()
                ))
                .build();
    }
}
