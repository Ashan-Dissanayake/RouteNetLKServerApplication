package lk.ashan.routenetlkserverapllication.module.branch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BranchController.class)
@Import(TestSecurityConfiguration.class)
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BranchService branchService;

    @Autowired
    private ObjectMapper objectMapper;


    // ==================== GET /branches ====================

    @Test
    void getBranches_ShouldReturn200() throws Exception {

        when(branchService.getBranches())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/branches")
                                .with(authority("branch-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(branchService).getBranches();
    }

    @Test
    void getBranches_ShouldReturn403_WhenUserDoesNotHaveBranchViewAuthority()
            throws Exception {

        mockMvc.perform(
                        get("/branches")
                                .with(authority("vehicle-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(branchService);
    }

    @Test
    void getBranches_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        get("/branches")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(branchService);
    }


    // ==================== GET /branches/summaries ====================

    @Test
    void getSummaries_ShouldReturn200_WhenAuthenticated() throws Exception {

        when(branchService.getSummaryBranches())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/branches/summaries")
                                .with(authenticatedUser())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(branchService).getSummaryBranches();
    }


    // ==================== POST /branches ====================

    @Test
    void createBranch_ShouldReturn201_WhenRequestIsValid() throws Exception {

        BranchCreateRequestDto request = validCreateRequest();

        when(branchService.createBranch(any(BranchCreateRequestDto.class)))
                .thenReturn(branchResponse());

        mockMvc.perform(
                        post("/branches")
                                .with(authority("branch-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.meta.status").value("created"));

        verify(branchService).createBranch(any(BranchCreateRequestDto.class));
    }

    @Test
    void createBranch_ShouldReturn403_WhenUserLacksBranchAddAuthority()
            throws Exception {

        BranchCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post("/branches")
                                .with(authority("branch-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(branchService);
    }

    @Test
    void createBranch_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        BranchCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post("/branches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(branchService);
    }

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createBranch_ShouldReturn400_WhenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<BranchCreateRequestDto> mutator) throws Exception {

        BranchCreateRequestDto request = validCreateRequest();

        mutator.accept(request);

        mockMvc.perform(
                        post("/branches")
                                .with(authority("branch-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(branchService);
    }

    static Stream<Arguments> missingFieldProvider() {

        return Stream.of(
                Arguments.of(
                        "name",
                        "Name is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setName(null)
                ),

                Arguments.of(
                        "code",
                        "Code is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setCode(null)
                ),

                Arguments.of(
                        "address",
                        "Address is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setAddress(null)
                ),

                Arguments.of(
                        "telephone",
                        "Telephone number is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setTelephone(null)
                ),

                Arguments.of(
                        "email",
                        "Email is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setEmail(null)
                ),

                Arguments.of(
                        "docreated",
                        "Created date is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setDocreated(null)
                ),

                Arguments.of(
                        "branchtype",
                        "Branch type is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setBranchtype(null)
                ),

                Arguments.of(
                        "branchstatus",
                        "Branch status is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setBranchstatus(null)
                ),

                Arguments.of(
                        "regionaloffice",
                        "Regional office is mandatory",
                        (Consumer<BranchCreateRequestDto>)
                                dto -> dto.setRegionaloffice(null)
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Branch@",
            "Branch#Name",
            "ThisNameIsWayTooLongToBeAcceptedBecauseItExceeds"
    })
    void createBranch_ShouldReturn400_WhenNameIsInvalid(
            String invalidName) throws Exception {

        BranchCreateRequestDto request = validCreateRequest();
        request.setName(invalidName);

        mockMvc.perform(
                        post("/branches")
                                .with(authority("branch-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("name: Invalid branch name format")));

        verifyNoInteractions(branchService);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789",
            "07123456789",
            "081234567",
            "01123456789",
            "07123A5678",
            " 0712345678",
            "0712345678 ",
            "+9471234567",
            "+941123456789",
            "+940712345678",
            "0723456789a"
    })
    void createBranch_ShouldReturn400_WhenTelephoneIsInvalid(
            String invalidPhone) throws Exception {

        BranchCreateRequestDto request = validCreateRequest();
        request.setTelephone(invalidPhone);

        mockMvc.perform(
                        post("/branches")
                                .with(authority("branch-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString("Invalid telephone number")
                        )));

        verifyNoInteractions(branchService);
    }

    @Test
    void createBranch_ShouldReturn400_WhenCreationDateIsInFuture()
            throws Exception {

        BranchCreateRequestDto request = validCreateRequest();
        request.setDocreated(LocalDate.now().plusDays(1));

        mockMvc.perform(
                        post("/branches")
                                .with(authority("branch-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString(
                                        "Creation date cannot be in the future"
                                )
                        )));

        verifyNoInteractions(branchService);
    }


    // ==================== PUT /branches ====================

    @Test
    void updateBranch_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        BranchUpdateRequestDto request = validUpdateRequest();

        when(branchService.updateBranch(any(BranchUpdateRequestDto.class)))
                .thenReturn(branchResponse());

        mockMvc.perform(
                        put("/branches")
                                .with(authority("branch-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk());

        verify(branchService)
                .updateBranch(any(BranchUpdateRequestDto.class));
    }

    @Test
    void updateBranch_ShouldReturn400_WhenRequestIsInvalid()
            throws Exception {

        BranchUpdateRequestDto request = validUpdateRequest();
        request.setName(null);

        mockMvc.perform(
                        put("/branches")
                                .with(authority("branch-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("name: Name is mandatory")));

        verifyNoInteractions(branchService);
    }

    @Test
    void updateBranch_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        BranchUpdateRequestDto request = validUpdateRequest();

        mockMvc.perform(
                        put("/branches")
                                .with(authority("branch-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(branchService);
    }


    // ==================== Test Data ====================

    private BranchCreateRequestDto validCreateRequest() {

        return BranchCreateRequestDto.builder()
                .name("Dambulla")
                .code("DML0001")
                .address("Kandy Road, Dambulla")
                .telephone("0665714150")
                .email("dbl@sltb.lk")
                .docreated(LocalDate.now().minusDays(200))
                .branchtype(
                        BranchTypeDto.builder()
                                .id(1)
                                .build()
                )
                .branchstatus(
                        BranchStatusDto.builder()
                                .id(1)
                                .build()
                )
                .regionaloffice(
                        RegionalOfficeDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }

    private BranchUpdateRequestDto validUpdateRequest() {

        return BranchUpdateRequestDto.builder()
                .id(1)
                .name("Colombo Head Office")
                .code("CLM0001")
                .address("Kirula Rd, Colombo 00500")
                .telephone("0112345678")
                .email("clm@sltb.lk")
                .docreated(LocalDate.now().minusDays(100))
                .branchtype(
                        BranchTypeDto.builder()
                                .id(1)
                                .build()
                )
                .branchstatus(
                        BranchStatusDto.builder()
                                .id(1)
                                .build()
                )
                .regionaloffice(
                        RegionalOfficeDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }

    private BranchDetailResponseDto branchResponse() {

        return BranchDetailResponseDto.builder()
                .id(1)
                .name("Dambulla")
                .code("DML0001")
                .address("Kandy Road, Dambulla")
                .telephone("0665714150")
                .email("dbl@sltb.lk")
                .docreated(LocalDate.now())
                .branchtype(
                        BranchTypeDto.builder()
                                .id(1)
                                .build()
                )
                .branchstatus(
                        BranchStatusDto.builder()
                                .id(1)
                                .build()
                )
                .regionaloffice(
                        RegionalOfficeDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }


    // ==================== Helpers ====================

    private RequestPostProcessor authenticatedUser() {
        return user("testuser");
    }

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
