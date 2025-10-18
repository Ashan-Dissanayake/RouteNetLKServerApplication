package lk.ashan.routenetlkserverapllication.module.branch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.dto.*;
import lk.ashan.routenetlkserverapllication.util.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.util.factory.DtoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Jackson mapper

    private final String apiUrl = "/branches"; // adjust path
    private BranchCreateRequestDto createRequestDto;

    @BeforeEach
    void setUp() {
        createRequestDto = DtoFactory.createBranchRequest("Colombo","CLB0005","0665714180");
    }

    @Test
    void createBranch_shouldSucceed_whenNameUpdated() throws Exception {

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Colombo"))
                .andExpect(jsonPath("$.data.branchcoverages.length()").value(2))
                .andExpect(jsonPath("$.data.branchtype.name").value("Region"))
                .andExpect(jsonPath("$.data.branchstatus.name").value("Active"));
    }

    @Test
    void createBranch_missingName_shouldReturnBadRequest() throws Exception {
        createRequestDto.setName(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "name: Branch name is mandatory"
                ));

    }

    @Test
    void createBranch_missingCode_shouldReturnBadRequest() throws Exception {

        createRequestDto.setCode(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "code: Branch code is mandatory"
                ));
    }

    @Test
    void createBranch_missingAddress_shouldReturnBadRequest() throws Exception{
        createRequestDto.setAddress(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Branch address is mandatory"
                ));
    }

    @Test
    void createBranch_missingTelephone_shouldReturnBadRequest() throws Exception{
        createRequestDto.setTelephone(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "telephone: Branch telephone number is mandatory"
                ));
    }

    @Test
    void createBranch_missingEmail_shouldReturnBadRequest() throws Exception{

        createRequestDto.setEmail(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "email: Branch email is mandatory"
               ));
    }

    @Test
    void createBranch_missingDocreated_shouldReturnBadRequest() throws Exception{
        createRequestDto.setDocreated(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "docreated: Branch date of created is mandatory"
                ));
    }

    @Test
    void createBranch_futureDate_shouldReturnBadRequest() throws Exception {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        createRequestDto.setDocreated(tomorrow);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "docreated: Creation date cannot be in the future"
                ));
    }

    @Test
    void createBranch_pastDate_shouldReturnCreated() throws Exception {

        createRequestDto.setDocreated(LocalDate.now().minusYears(1));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createBranch_todayDate_shouldReturnCreated() throws Exception {
        createRequestDto.setDocreated(LocalDate.now());

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createBranch_missingBranchType_shouldReturnBadRequest() throws Exception{
        createRequestDto.setBranchtype(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branchtype: Branch type is mandatory"
                ));
    }

    @Test
    void createBranch_missingBranchStatus_shouldReturnBadRequest() throws Exception{

        createRequestDto.setBranchstatus(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branchstatus: Branch status is mandatory"
                ));
    }

    @Test
    void createBranch_missingBranchCoverages_shouldReturnBadRequest() throws Exception{
        createRequestDto.setBranchcoverages(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branchcoverages: Branch coverages are mandatory"
                ));
    }

    @Test
    void createBranch_deactivatedExistingName_shouldReturnBadRequest() throws Exception {

        createRequestDto = DtoFactory.branchSoftDeletedCreateRequest(
                "Kesbewa Deport",
                "ABC0005",
                "Piliyandala",
                "0117706888",
                "abc@sltb.lk"
        );

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError("Branch name already exists.")
                );

    }


    @Test
    void createBranch_deactivatedExistingCode_shouldReturnBadRequest() throws Exception {

        createRequestDto = DtoFactory.branchSoftDeletedCreateRequest(
                "Abc Deport",
                "KSB0005",
                "Piliyandala",
                "0117706888",
                "abc@sltb.lk"
        );

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError("Branch code already exists.")
                );

    }

    @Test
    void createBranch_deactivatedExistingEmail_shouldReturnBadRequest() throws Exception {

        createRequestDto = DtoFactory.branchSoftDeletedCreateRequest(
                "Abc Deport",
                "ABC0005",
                "Piliyandala",
                "0117706888",
                "ksb@sltb.lk"
        );

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError("Branch email already exists.")
                );

    }

    @Test
    void createBranch_deactivatedExistingTelephone_shouldReturnBadRequest() throws Exception {

        createRequestDto = DtoFactory.branchSoftDeletedCreateRequest(
                "Abc Deport",
                "ABC0005",
                "Piliyandala",
                "0117706360",
                "abc@sltb.lk"
        );

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError("Branch telephone already exists.")
                );

    }


}
