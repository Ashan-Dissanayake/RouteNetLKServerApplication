package lk.ashan.routenetlkserverapllication.module.branch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.dto.*;
import lk.ashan.routenetlkserverapllication.util.ValidationResultMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Test
    void createBranch_shouldSucceed_whenAllCorrect() throws Exception {
        BranchCreateRequestDto request = BranchCreateRequestDto.builder()
                .name("Dambulla Branch")
                .code("DMB0010")
                .address("No.12 Kandy Road, Dambulla")
                .telephone("0665714120")
                .docreated(Date.valueOf("2025-09-18"))
                .email("dmbl@ntc.lk")
                .remarks("")
                .branchtype(BranchtypeDto.builder().id(2).name("Region").build())
                .branchstatus(BranchstatusDto.builder().id(1).name("Active").build())
                .branchcoverages(Arrays.asList(
                        BranchDistrictCoverageDto.builder()
                                .district(DistrictDto.builder()
                                        .id(4)
                                        .name("Kandy")
                                        .province(ProvinceDto.builder().id(1).name("Central").build())
                                        .build())
                                .build(),
                        BranchDistrictCoverageDto.builder()
                                .district(DistrictDto.builder()
                                        .id(5)
                                        .name("Matale")
                                        .province(ProvinceDto.builder().id(1).name("Central").build())
                                        .build())
                                .build()
                ))
                .build();

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Dambulla Branch"))
                .andExpect(jsonPath("$.data.branchcoverages.length()").value(2))
                .andExpect(jsonPath("$.data.branchtype.name").value("Region"))
                .andExpect(jsonPath("$.data.branchstatus.name").value("Active"));
    }

    @Test
    void createBranch_missingName_shouldReturnBadRequest() throws Exception {
        BranchCreateRequestDto request = validRequest();
        request.setName(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "name: Branch name is mandatory"
                ));

    }

    @Test
    void createBranch_missingCode_shouldReturnBadRequest() throws Exception {
        BranchCreateRequestDto request = validRequest();
        request.setCode(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "code: Branch code is mandatory"
                ));
    }

    private BranchCreateRequestDto validRequest() {
        return BranchCreateRequestDto.builder()
                .name("Dambulla Branch")
                .code("DMB0010")
                .address("No.12 Kandy Road, Dambulla")
                .telephone("0665714120")
                .docreated(Date.valueOf("2025-09-18"))
                .remarks("Test")
                .branchtype(new BranchtypeDto(1, "Region"))
                .branchstatus(new BranchstatusDto(1, "Active"))
                .branchcoverages(List.of(
                        new BranchDistrictCoverageDto(1, new DistrictDto(4, "Kandy", new ProvinceDto(1, "Central")))
                ))
                .build();
    }
}
