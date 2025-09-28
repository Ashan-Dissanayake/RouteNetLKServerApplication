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
    private BranchCreateRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = DtoFactory.createBranchRequest("Dambulla","DMB0010","0665714120");
    }

    @Test
    void createBranch_shouldSucceed_whenAllCorrect() throws Exception {

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Dambulla"))
                .andExpect(jsonPath("$.data.branchcoverages.length()").value(1))
                .andExpect(jsonPath("$.data.branchtype.name").value("Region"))
                .andExpect(jsonPath("$.data.branchstatus.name").value("Active"));
    }

    @Test
    void createBranch_missingName_shouldReturnBadRequest() throws Exception {
        requestDto.setName(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "name: Branch name is mandatory"
                ));

    }

    @Test
    void createBranch_missingCode_shouldReturnBadRequest() throws Exception {

        requestDto.setCode(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "code: Branch code is mandatory"
                ));
    }

    @Test
    void createBranch_missingAddress_shouldReturnBadRequest() throws Exception{
        requestDto.setAddress(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Branch address is mandatory"
                ));
    }

    @Test
    void createBranch_missingTelephone_shouldReturnBadRequest() throws Exception{
        requestDto.setTelephone(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "telephone: Branch telephone number is mandatory"
                ));
    }

    @Test
    void createBranch_missingEmail_shouldReturnBadRequest() throws Exception{

        requestDto.setEmail(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "email: Branch email is mandatory"
               ));
    }

    @Test
    void createBranch_missingDocreated_shouldReturnBadRequest() throws Exception{
        requestDto.setDocreated(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "docreated: Branch date of created is mandatory"
                ));
    }

    @Test
    void createBranch_futureDate_shouldReturnBadRequest() throws Exception {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        requestDto.setDocreated(tomorrow);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "docreated: Creation date cannot be in the future"
                ));
    }

    @Test
    void createBranch_pastDate_shouldReturnCreated() throws Exception {

        requestDto.setDocreated(LocalDate.now().minusYears(1));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createBranch_todayDate_shouldReturnCreated() throws Exception {
        requestDto.setDocreated(LocalDate.now());

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createBranch_missingBranchType_shouldReturnBadRequest() throws Exception{
        requestDto.setBranchtype(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branchtype: Branch type is mandatory"
                ));
    }

    @Test
    void createBranch_missingBranchStatus_shouldReturnBadRequest() throws Exception{

        requestDto.setBranchstatus(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branchstatus: Branch status is mandatory"
                ));
    }

    @Test
    void createBranch_missingBranchCoverages_shouldReturnBadRequest() throws Exception{

        requestDto.setBranchcoverages(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branchcoverages: Branch coverages are mandatory"
                ));
    }


}
