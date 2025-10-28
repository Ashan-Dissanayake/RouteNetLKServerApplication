package lk.ashan.routenetlkserverapllication.module.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.util.factory.DtoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Jackson mapper

    private static final String apiUrl = "/employees";


    @Test
    void createEmployee_shouldSucceed_whenUniqueEmployeeRequestWithoutImage() throws Exception {
        // Arrange: Prepare valid, unique employee creation request (no image)
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();

        // Act & Assert: Perform POST request and verify response
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.fullname").value("Minuri Navoddika"))
                .andExpect(jsonPath("$.data.number").value("EMPCLM0007"))
                .andExpect(jsonPath("$.data.nic").value("200223171988"))
                .andExpect(jsonPath("$.data.branch.name").value("Avissawella"))
                .andExpect(jsonPath("$.data.designation.name").value("Assistant Manager"))
                .andExpect(jsonPath("$.data.department.name").value("Administrative "))
                .andExpect(jsonPath("$.data.employeetype.name").value("Permanent"))
                .andExpect(jsonPath("$.data.employeestatus.name").value("Active"));
    }
}
