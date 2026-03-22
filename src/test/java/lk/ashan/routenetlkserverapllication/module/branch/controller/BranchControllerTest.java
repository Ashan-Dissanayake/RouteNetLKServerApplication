package lk.ashan.routenetlkserverapllication.module.branch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/branches";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createBranch_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<BranchCreateRequestDto> mutator) throws Exception {
        BranchCreateRequestDto dto = BranchCreateRequestDto.builder()
                .name("Dambulla")
                .code("DML0001")
                .address("Kandy Road, Dambulla")
                .telephone("0665714150")
                .email("dbl@sltb.lk")
                .docreated(LocalDate.now().minusDays(200))
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingFieldProvider() {
        return Stream.of(
                Arguments.of("name", "Name is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setName(null)),
                Arguments.of("code", "Code is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setCode(null)),
                Arguments.of("address", "Address is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setAddress(null)),
                Arguments.of("telephone", "Telephone number is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setTelephone(null)),
                Arguments.of("email", "Email is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setEmail(null)),
                Arguments.of("docreated", "Created date is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setDocreated(null)),
                Arguments.of("branchtype", "Branch type is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setBranchtype(null)),
                Arguments.of("branchstatus", "Branch status is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setBranchstatus(null)),
                Arguments.of("regionaloffice", "Regional office is mandatory", (Consumer<BranchCreateRequestDto>) dto -> dto.setRegionaloffice(null))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",                             // blank
            "Branch@",                       // invalid character
            "Branch#Name",                   // invalid character
            "ThisNameIsWayTooLongToBeAcceptedBecauseItExceeds"
    })
    void createBranch_shouldFail_whenNameIsInvalid(String invalidName) throws Exception {
        BranchCreateRequestDto dto = BranchCreateRequestDto.builder()
                .name(invalidName)
                .code("DML0001")
                .address("Kandy Road, Dambulla")
                .telephone("0665714150")
                .email("dbl@sltb.lk")
                .docreated(LocalDate.now().minusDays(200))
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasItem("name" + ": " + "Invalid branch name format")));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "123456789",         // too short
            "07123456789",       // 11 digits (too long)
            "081234567",         // 9 digits (too short for landline)
            "01123456789",       // 11 digits (too long for landline)
            "07123A5678",        // letter included
            " 0712345678",       // leading space
            "0712345678 ",       // trailing space
            "+9471234567",       // mobile missing 1 digit
            "+941123456789",     // landline too long
            "+940712345678",     // invalid international prefix format
            "0723456789a"        // letter at the end
    })
    void createBranch_shouldFail_whenTelephoneIsInvalid(String invalidPhone) throws Exception {
        BranchCreateRequestDto dto = BranchCreateRequestDto.builder()
                .name("Dambulla")
                .code("DML0001")
                .address("Kandy Road, Dambulla")
                .telephone(invalidPhone)
                .email("dbl@sltb.lk")
                .docreated(LocalDate.now().minusDays(200))
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasItem(containsString("Invalid telephone number"))));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AddressWithInvalidChar#",               // invalid character
    })
    void createBranch_shouldFail_whenAddressIsInvalid(String invalidAddress) throws Exception {
        BranchCreateRequestDto dto = BranchCreateRequestDto.builder()
                .name("Dambulla")
                .code("DML0001")
                .address(invalidAddress)
                .telephone("0665714150")
                .email("dbl@sltb.lk")
                .docreated(LocalDate.now().minusDays(200))
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasItem(containsString("Invalid address format"))));
    }

    @ParameterizedTest
    @MethodSource("doCreatedTestCases")
    void createBranch_doCreatedValidation_shouldSuccess(
            LocalDate inputDate,
            ResultMatcher expectedStatus,
            String expectedError
    ) throws Exception {

        BranchCreateRequestDto dto = BranchCreateRequestDto.builder()
                .name("Dambulla")
                .code("DML0001")
                .address("Kandy Road, Dambulla")
                .telephone("0665714150")
                .email("dbl@sltb.lk")
                .docreated(inputDate)
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).name("Active").build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();
        ResultActions resultActions = mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(expectedStatus);

        if (expectedError != null) {
            resultActions.andExpect(
                    jsonPath("$.details", hasItem(containsString(expectedError)))
            );
        }
    }

    private static Stream<Arguments> doCreatedTestCases() {
        return Stream.of(
                Arguments.of(
                        LocalDate.now().plusDays(1),
                        status().isBadRequest(),
                        "docreated: Creation date cannot be in the future"
                ),
                Arguments.of(
                        LocalDate.now().minusYears(1),
                        status().isCreated(),
                        null
                ),
                Arguments.of(
                        LocalDate.now(),
                        status().isCreated(),
                        null
                )
        );
    }

    @ParameterizedTest
    @MethodSource("duplicateFieldProvider")
    @Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBranch_shouldFail_whenDuplicateField(
            String expectedError,
            Consumer<BranchCreateRequestDto> mutator
    ) throws Exception {

        BranchCreateRequestDto dto = BranchCreateRequestDto.builder()
                .name("New Branch")
                .code("NEW0001")
                .address("New Address")
                .telephone("0716042674")
                .email("new@sltb.lk")
                .docreated(LocalDate.now().minusDays(10))
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value(expectedError));
    }

   private static Stream<Arguments> duplicateFieldProvider() {
        return Stream.of(
                Arguments.of(
                        "Branch code already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setCode("CLM0001")
                ),
                Arguments.of(
                        "Branch name already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setName("Angoda")
                ),
                Arguments.of(
                        "Branch email already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setEmail("clm@sltb.lk")
                ),
                Arguments.of(
                        "Branch telephone already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setTelephone("0117706320")
                ),
                Arguments.of(
                        "Address already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setAddress("Kirula Rd, Colombo 00500")
                )
        );
    }


    @ParameterizedTest
    @MethodSource("deactivatedDuplicateFieldProvider")
    @Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBranch_shouldFail_whenDeactivatedDuplicateField(
            String expectedError,
            Consumer<BranchCreateRequestDto> mutator
    ) throws Exception {

        BranchCreateRequestDto dto = BranchCreateRequestDto.builder()
                .name("New Branch")
                .code("NEW0001")
                .address("New Address")
                .telephone("0716042674")
                .email("new@sltb.lk")
                .docreated(LocalDate.now().minusDays(10))
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value(expectedError));
    }

    private static Stream<Arguments> deactivatedDuplicateFieldProvider() {
        return Stream.of(
                Arguments.of(
                        "Branch code already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setCode("AVS0001")
                ),
                Arguments.of(
                        "Branch name already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setName("Avissawella")
                ),
                Arguments.of(
                        "Branch email already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setEmail("avs@sltb.lk")
                ),
                Arguments.of(
                        "Branch telephone already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setTelephone("0362222348")
                ),
                Arguments.of(
                        "Address already exists.",
                        (Consumer<BranchCreateRequestDto>) dto -> dto.setAddress("X644 42W, Road, Avissawella")
                )
        );
    }

    @Test
    void updateBranch_shouldFail_whenCodeIsChanged() throws Exception {
        BranchUpdateRequestDto dto = BranchUpdateRequestDto.builder()
                .id(2)
                .name("Angoda")
                .code("AGD0001")
                .address("WWF7 2H4, Colombo")
                .telephone("0117706321")
                .email("ang@sltb.lk")
                .docreated(LocalDate.parse("2025-10-03"))
                .branchtype(BranchTypeDto.builder().id(3).build())
                .branchstatus(BranchStatusDto.builder().id(1).name("Active").build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details", hasItem("Code cannot be changed")));
    }

    @Test
    @Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateBranch_shouldSucceed_whenSameValuesUsed() throws Exception {
        BranchUpdateRequestDto dto = BranchUpdateRequestDto.builder()
                .id(2)
                .name("Angoda")
                .code("ANG0001")
                .address("WWF7 2H4, Colombo")
                .telephone("0117706321")
                .email("ang@sltb.lk")
                .docreated(LocalDate.parse("2025-10-03"))
                .branchtype(BranchTypeDto.builder().id(3).build())
                .branchstatus(BranchStatusDto.builder().id(1).name("Active").build())
                .regionaloffice(RegionalOfficeDto.builder().id(9).build())
                .build();
        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("duplicateFieldProviderForUpdate")
    @Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateBranch_shouldFail_whenDuplicateField(
            String expectedError,
            Consumer<BranchUpdateRequestDto> mutator
    ) throws Exception {

        BranchUpdateRequestDto dto = BranchUpdateRequestDto.builder()
                .id(1)
                .name("Colombo head office")
                .code("CLM0001")
                .address("Kirula Rd, Colombo 00500")
                .telephone("0117706320")
                .email("clm@sltb.lk")
                .docreated(LocalDate.parse("2025-10-03"))
                .branchtype(BranchTypeDto.builder().id(1).build())
                .branchstatus(BranchStatusDto.builder().id(1).build())
                .regionaloffice(RegionalOfficeDto.builder().id(1).build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details").value(expectedError));
    }

    private static Stream<Arguments> duplicateFieldProviderForUpdate() {
        return Stream.of(
                Arguments.of(
                        "Another branch already uses this name.",
                        (Consumer<BranchUpdateRequestDto>) dto -> dto.setName("Angoda")
                ),
                Arguments.of(
                        "Another branch already uses this email.",
                        (Consumer<BranchUpdateRequestDto>) dto -> dto.setEmail("ang@sltb.lk")
                ),
                Arguments.of(
                        "Another branch already uses this telephone.",
                        (Consumer<BranchUpdateRequestDto>) dto -> dto.setTelephone("0117706321")
                ),
                Arguments.of(
                        "Another branch in this address.",
                        (Consumer<BranchUpdateRequestDto>) dto -> dto.setAddress("WWF7 2H4, Colombo")
                )
        );
    }

    @Test
    @Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateBranch_shouldSucceed_whenActiveToSuspended() throws Exception {

        BranchUpdateRequestDto dto = BranchUpdateRequestDto.builder()
                .id(2)
                .name("Angoda")
                .code("ANG0001")
                .address("WWF7 2H4, Colombo")
                .telephone("0117706321")
                .email("ang@sltb.lk")
                .docreated(LocalDate.parse("2025-10-03"))
                .branchtype(BranchTypeDto.builder().id(3).build())
                .branchstatus(BranchStatusDto.builder().id(1).name("Active").build())
                .regionaloffice(RegionalOfficeDto.builder().id(9).build())
                .build();

        dto.setBranchstatus(BranchStatusDto.builder().id(2).name("Suspended").build());

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
