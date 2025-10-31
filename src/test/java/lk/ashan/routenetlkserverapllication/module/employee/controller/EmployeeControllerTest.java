package lk.ashan.routenetlkserverapllication.module.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.util.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.util.factory.DtoFactory;
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

    @Test
    void createEmployee_shouldFail_whenNumberIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setNumber(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Number mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenFullNameIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setFullname(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "fullname: Full name is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenCallingNameIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setCallingname(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "callingname: Calling name is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNicIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setNic(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "nic: NIC is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenGenderIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setGender(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "gender: Gender is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setMobile(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Mobile is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmailIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmail(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "email: Email is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenAddressIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setAddress(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Address is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmergencycontact(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Emergency Contact is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDojIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "doj: Date of joined is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDojIsFutureDate() throws Exception{

        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(LocalDate.parse("2025-11-01"));

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "doj: Joined date cannot be in the future"
                ));

    }

    @Test
    void createEmployee_shouldSucceed_whenDojIsPastDate() throws Exception{

        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(LocalDate.parse("2015-11-01"));

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createEmployee_shouldSucceed_whenDojIsToday() throws Exception{

        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(LocalDate.now());

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createEmployee_shouldFail_whenBranchIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setBranch(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branch: Branch is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDepartmentIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDepartment(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "department: Department is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDesignationIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDesignation(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "designation: Designation is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmployeetypeIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmployeetype(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "employeetype: Employee Type is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmployeestatusIsMissing() throws Exception {
        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmployeestatus(null);

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "employeestatus: Employee Status is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDuplicateNumber() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createExistEmployeeRequestNoImage();
        employeeCreateRequestDto.setNic("200223171988");//Unique NIC
        employeeCreateRequestDto.setMobile("0716042647");//Unique mobile
        employeeCreateRequestDto.setEmail("minuri.EMPCLM0007@sltb.lk");//Unique Email

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Employee number already exists."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDuplicateNic() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createExistEmployeeRequestNoImage();
        employeeCreateRequestDto.setNumber("EMPCLM0007");//Unique Number
        employeeCreateRequestDto.setMobile("0716042647");//Unique mobile
        employeeCreateRequestDto.setEmail("minuri.EMPCLM0007@sltb.lk");//Unique Email

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "NIC already exists."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDuplicateMobile() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createExistEmployeeRequestNoImage();
        employeeCreateRequestDto.setNumber("EMPCLM0007");//Unique Number
        employeeCreateRequestDto.setNic("200223171988");//Unique NIC
        employeeCreateRequestDto.setEmail("minuri.EMPCLM0007@sltb.lk");//Unique Email

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Mobile number already exists."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDuplicateEmail() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createExistEmployeeRequestNoImage();
        employeeCreateRequestDto.setNumber("EMPCLM0007");//Unique Number
        employeeCreateRequestDto.setNic("200223171988");//Unique NIC
        employeeCreateRequestDto.setMobile("0716042647");//Unique mobile

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Email already exists."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileAndEmergencyContactAreSame() throws Exception {
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmergencycontact("0716042647");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Employee mobile number and emergency contact cannot be the same."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileAlreadyUsedAsEmergencyContactByAnotherEmployee() throws Exception {
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setMobile("0725566778");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Mobile number already used as emergency contact by another employee."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencyContactAlreadyUsedAsMobileByAnotherEmployee() throws Exception {
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmergencycontact("0757788990");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Emergency contact already used as another employee’s mobile number."
                ));
    }



}
