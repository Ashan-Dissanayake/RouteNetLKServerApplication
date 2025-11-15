package lk.ashan.routenetlkserverapllication.module.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeUpdateRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
                .andExpect(jsonPath("$.data.nic").value("200253171988"))
                .andExpect(jsonPath("$.data.branch.name").value("Avissawella"))
                .andExpect(jsonPath("$.data.designation.name").value("Assistant Manager"))
                .andExpect(jsonPath("$.data.department.name").value("Administrative "))
                .andExpect(jsonPath("$.data.employeetype.name").value("Permanent"))
                .andExpect(jsonPath("$.data.employeestatus.name").value("Active"));
    }

    //Testing for mandatory field validation
    @Test
    void createEmployee_shouldFail_whenNumberIsMissing() throws Exception {
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setNumber(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setFullname(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setCallingname(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setNic(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setGender(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setMobile(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmail(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setAddress(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmergencycontact(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "doj: Date of joined is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenBranchIsMissing() throws Exception {
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setBranch(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDepartment(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDesignation(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmployeetype(null);

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
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmployeestatus(null);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "employeestatus: Employee Status is mandatory"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenDojIsFutureDate() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(LocalDate.now().plusDays(1));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "doj: Joined date cannot be in the future"
                ));

    }

    @Test
    void createEmployee_shouldSucceed_whenDojIsPastDate() throws Exception {

        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(LocalDate.parse("2015-11-01"));

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isCreated());
    }

    //DOJ range validation
    @Test
    void createEmployee_shouldSucceed_whenDojIsToday() throws Exception {

        // Arrange: Prepare a valid employee request, then remove the mandatory employee number
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setDoj(LocalDate.now());

        // Act & Assert: Perform POST request and verify it fails with proper validation error
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isCreated());
    }

    //Unique field validation
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

    //Pattern validation-Number
    @Test
    void createEmployee_shouldFail_whenNumberHasTooFewLetters() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMPAB1234");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberHasTooManyLetters() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMPABCD1234");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberHasLowercasePrefix() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("empABC1234");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberHasLowercaseLetters() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMPabc1234");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberHasTooFewDigits() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMPABC123");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberHasTooManyDigits() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMPABC12345");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberHasNonDigitCharactersInSuffix() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMPABC12A4");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberContainsSymbol() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMPABC12@4");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberContainsSpace() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMP ABC1234");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNumberContainsHyphen() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNumber("EMP-ABC1234");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid employee number"
                ));
    }

    //Pattern validation-Full Name
    @Test
    void createEmployee_shouldFail_whenFullnameIsInvalidFormat() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setFullname("Minuri N@vodd1kA");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "fullname: Invalid full name"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenCallingnameIsInvalidFormat() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setCallingname("N@vodd1kA");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "callingname: Invalid calling name"
                ));
    }

    //Pattern validation-NIC
    @Test
    void createEmployee_shouldSucceed_whenNicIsOldFormat() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setNic("995412786V");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createEmployee_shouldSucceed_whenNicIsNewFormat() throws Exception {

        //createUniqueEmployeeRequestNoImage() returns object with nic is new format
        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createEmployee_shouldFail_whenOldNicIsInvalidFormat() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setNic("9a41A7@6x");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "nic: Invalid NIC"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenNewNicIsInvalidFormat() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setNic("200@23A7x98");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "nic: Invalid NIC"
                ));
    }

    //Pattern validation-Mobile
    @Test
    void createEmployee_shouldFail_whenMobileHasInvalidPrefix() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("0791234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileIsTooShort() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("07123456");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileIsTooLong() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("07123456789");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileStartsWithCountryCode() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("+94771234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileContainsHyphen() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("071-1234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileContainsSpace() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("071 1234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileContainsLetter() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("071A234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenMobileContainsSymbol() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setMobile("071@234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    //Pattern validation-Email
    @Test
    void createEmployee_shouldFail_whenEmailIsInvalidFormat() throws Exception {

        EmployeeCreateRequestDto employeeCreateRequestDto = DtoFactory.createUniqueEmployeeRequestNoImage();
        employeeCreateRequestDto.setEmail("minuri.EMPCLM0007#sltb.lk");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "email: Invalid email format"
                ));
    }

    //Pattern validation-Address
    @Test
    void createEmployee_shouldFail_whenAddressContainsHash() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setAddress("#12, Flower Rd");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenAddressContainsAtSymbol() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setAddress("12@Main Street");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenAddressContainsPeriod() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setAddress("Colombo 07.");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenAddressContainsQuotes() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setAddress("“No 5”");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenAddressContainsUnicodeSinhala() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setAddress("අංක 12, පාර");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenAddressContainsAsterisk() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setAddress("12* Main Rd");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenAddressIsTooShort() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setAddress("A");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    //Pattern validation-Emergency Contact
    @Test
    void createEmployee_shouldFail_whenEmergencycontactIsTooShort() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("07123456");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactIsTooLong() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("07123456789");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactHasInvalidMobilePrefix() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("0791234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactHasInvalidLandlinePrefix() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("0491234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactStartsWithCountryCode() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("+94771234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactContainsHyphen() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("071-1234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactContainsSpace() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("071 1234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactContainsLetter() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("071A234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void createEmployee_shouldFail_whenEmergencycontactContainsSymbol() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmergencycontact("071@234567");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    //Flow
    @Test
    void createEmployee_shouldAutoCorrectEmail_whenEmailIsIncorrect() throws Exception {
        // Arrange
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmail("wrong.email@sltb.lk"); // deliberately incorrect

        String expectedEmail = "minuri.EMPCLM0007@sltb.lk";

        // Act & Assert
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(expectedEmail));
    }

    @Test
    void createEmployee_shouldFail_whenGenderDoesNotMatchNIC() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setNic("200012345678"); // dayCode < 500 → Male
        dto.setGender(DtoFactory.genderDto(2, "Female")); // mismatch

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Gender not match with given NIC"
                ));
    }

    @Test
    void createEmployee_shouldSucceed_whenDepartmentDesignationValid() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setDepartment(DtoFactory.departmentDto(1, "Operations (Traffic)"));
        dto.setDesignation(DtoFactory.designationDto(1, "Driver"));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createEmployee_shouldFail_whenAssistantManagerInWrongDepartment() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setDepartment(DtoFactory.departmentDto(3, "Finance and Revenue"));
        dto.setDesignation(DtoFactory.designationDto(4, "Assistant Manager"));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Invalid combination: Assistant Manager cannot belong to Finance and Revenue department."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenProbationerHasOldDOJ() throws Exception {
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmployeetype(DtoFactory.employeetypeDto(4, "Probationers"));
        dto.setDoj(LocalDate.of(2023, 5, 10)); // previous year

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Probationers employees cannot have a Date of Joining older than the current year (2025)."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenContractDOJOlderThanCurrentYear() throws Exception {
        // Arrange
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setEmployeetype(DtoFactory.employeetypeDto(2, "Contract"));
        dto.setDoj(LocalDate.of(2023, 5, 10)); // DOJ older than current year (e.g. 2025)

        // Act & Assert
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Contract employees cannot have a Date of Joining older than the current year (2025)."
                ));
    }

    @Test
    void createEmployee_shouldFail_whenGenderOfDriverIsFemale() throws Exception {

        // Arrange
        EmployeeCreateRequestDto dto = DtoFactory.createUniqueEmployeeRequestNoImage();
        dto.setDepartment(DtoFactory.departmentDto(1, "Operations (Traffic)"));
        dto.setDesignation(DtoFactory.designationDto(1, "Driver"));

        // Act & Assert
        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Female employees are not allowed to be a driver."
                ));

    }

    //Employee-Update Test

    //Mandatory

    //Full Name
    @Test
    void updateEmployee_shouldFail_whenFullNameIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setFullname(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "fullname: Full name is mandatory"
                ));
    }

    //Number
    @Test
    void updateEmployee_shouldFail_whenNumberIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNumber(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Number mandatory"
                ));
    }

    //Calling Name
    @Test
    void updateEmployee_shouldFail_whenCallingNameIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setCallingname(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "callingname: Calling name is mandatory"
                ));
    }

    //Nic
    @Test
    void updateEmployee_shouldFail_whenNicIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNic(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "nic: NIC is mandatory"
                ));
    }

    //Gender
    @Test
    void updateEmployee_shouldFail_whenGenderIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setGender(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "gender: Gender is mandatory"
                ));
    }

    //Mobile
    @Test
    void updateEmployee_shouldFail_whenMobileIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Mobile is mandatory"
                ));
    }

    //Email
    @Test
    void updateEmployee_shouldFail_whenEmailIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmail(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "email: Email is mandatory"
                ));
    }

    //Address
    @Test
    void updateEmployee_shouldFail_whenAddressIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Address is mandatory"
                ));
    }

    //Emergency Contact
    @Test
    void updateEmployee_shouldFail_whenEmergencyContactIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Emergency Contact is mandatory"
                ));
    }

    //Branch
    @Test
    void updateEmployee_shouldFail_whenBranchIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setBranch(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "branch: Branch is mandatory"
                ));
    }

    //Department
    @Test
    void updateEmployee_shouldFail_whenDepartmentIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setDepartment(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "department: Department is mandatory"
                ));
    }

    //Designation
    @Test
    void updateEmployee_shouldFail_whenDesignationIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setDesignation(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "designation: Designation is mandatory"
                ));
    }

    //Employee Type
    @Test
    void updateEmployee_shouldFail_whenEmployeeTypeIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmployeetype(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "employeetype: Employee Type is mandatory"
                ));
    }

    //Employee Status
    @Test
    void updateEmployee_shouldFail_whenEmployeeStatusIsMissing() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmployeestatus(null);

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "employeestatus: Employee Status is mandatory"
                ));
    }


    //Pattern validation

    //Full Name
    @Test
    void updateEmployee_shouldFail_whenFullnameIsInvalidFormat() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setFullname("Minuri N@vodd1kA");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "fullname: Invalid full name"
                ));
    }

    //Calling Name
    @Test
    void updateEmployee_shouldFail_whenCallingnameIsInvalidFormat() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setCallingname("N@vodd1kA");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "callingname: Invalid calling name"
                ));
    }

    //NIC
    @Test
    void updateEmployee_shouldSucceed_whenNicIsOldFormat() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNic("995412786V");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateEmployee_shouldSucceed_whenNicIsNewFormat() throws Exception {

        // createEmployeeUpateRequestNoImage() already returns new-format NIC by default
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateEmployee_shouldFail_whenOldNicIsInvalidFormat() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNic("9a41A7@6x");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "nic: Invalid NIC"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenNewNicIsInvalidFormat() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNic("200@23A7x98");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "nic: Invalid NIC"
                ));
    }

    //Mobile
    @Test
    void updateEmployee_shouldFail_whenMobileHasInvalidPrefix() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("0791234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileIsTooShort() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("07123456");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileIsTooLong() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("07123456789");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileStartsWithCountryCode() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("+94771234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileContainsHyphen() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("071-1234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileContainsSpace() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("071 1234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileContainsLetter() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("071A234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileContainsSymbol() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("071@234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "mobile: Invalid mobile number"
                ));
    }

    //Address
    @Test
    void updateEmployee_shouldFail_whenAddressContainsHash() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress("#12, Flower Rd");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenAddressContainsAtSymbol() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress("12@Main Street");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenAddressContainsPeriod() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress("Colombo 07.");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenAddressContainsQuotes() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress("“No 5”");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenAddressContainsUnicodeSinhala() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress("අංක 12, පාර");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenAddressContainsAsterisk() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress("12* Main Rd");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenAddressIsTooShort() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setAddress("A");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "address: Invalid address"
                ));
    }

    //Emergency contact
    @Test
    void updateEmployee_shouldFail_whenEmergencycontactIsTooShort() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("07123456");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactIsTooLong() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("07123456789");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactHasInvalidMobilePrefix() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("0791234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactHasInvalidLandlinePrefix() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("0491234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactStartsWithCountryCode() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("+94771234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactContainsHyphen() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("071-1234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactContainsSpace() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("071 1234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactContainsLetter() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("071A234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencycontactContainsSymbol() throws Exception {

        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("071@234567");

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "emergencycontact: Invalid emergency contact"
                ));
    }

    //Uniqueness
    @Test
    void updateEmployee_shouldFail_whenDuplicateNumber() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNic("200223171988");          // Unique NIC
        employeeUpdateRequestDto.setMobile("0716042647");         // Unique mobile
        employeeUpdateRequestDto.setEmail("minuri.EMPCLM0007@sltb.lk"); // Unique Email
        employeeUpdateRequestDto.setNumber("EMPCLM0009");         // Existing number to trigger conflict

        mockMvc.perform(put(apiUrl) // Replace 1 with the employee ID being updated
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Employee number already exists."
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenDuplicateNic() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNumber("EMPCLM0001");        // Unique Number
        employeeUpdateRequestDto.setMobile("0716042647");       // Unique mobile
        employeeUpdateRequestDto.setEmail("minuri.EMPCLM0007@sltb.lk"); // Unique Email
        employeeUpdateRequestDto.setNic("976543210V");        // Existing NIC to trigger conflict

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "NIC already exists."
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenDuplicateMobile() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setNumber("EMPCLM0001");       // Unique Number
        employeeUpdateRequestDto.setNic("200223171988");       // Unique NIC
        employeeUpdateRequestDto.setEmail("minuri.EMPCLM0007@sltb.lk"); // Unique Email
        employeeUpdateRequestDto.setMobile("0773333333");      // Existing mobile to trigger conflict

        mockMvc.perform(put(apiUrl )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Mobile number already exists."
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileAndEmergencyContactAreSame() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("0771234567"); // Same as mobile

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Employee mobile number and emergency contact cannot be the same."
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenMobileAlreadyUsedAsEmergencyContactByAnotherEmployee() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setMobile("0712334556"); // Used as emergency contact by another employee

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Mobile number already used as emergency contact by another employee."
                ));
    }

    @Test
    void updateEmployee_shouldFail_whenEmergencyContactAlreadyUsedAsMobileByAnotherEmployee() throws Exception {
        EmployeeUpdateRequestDto employeeUpdateRequestDto = DtoFactory.createEmployeeUpateRequestNoImage();
        employeeUpdateRequestDto.setEmergencycontact("0706677889"); // Used as mobile by another employee

        mockMvc.perform(put(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeUpdateRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Emergency contact already used as another employee’s mobile number."
                ));
    }



}
