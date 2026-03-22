package lk.ashan.routenetlkserverapllication.module.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Gender;
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

//@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles("test")
//@Transactional
class EmployeeControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/employees";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createEmployee_shouldFail_whenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<EmployeeCreateRequestDto> mutator
    ) throws Exception {

        EmployeeCreateRequestDto dto = EmployeeCreateRequestDto.builder()
                //.number("EMP0001")
                .fullname("John Silva")
                .callingname("John")
                .nic("200012345678")
                .gender(GenderDto.builder().id(1).build())
                .mobile("0712345678")
                //.email("john@sltb.lk")
                .address("Colombo")
                .emergencycontact("0771234567")
                .doj(LocalDate.now().minusDays(10))
                .branch(BranchSummaryDto.builder().id(1).build())
                .department(DepartmentDto.builder().id(1).build())
                .designation(DesignationDto.builder().id(1).build())
                .employeetype(EmployeeTypeDto.builder().id(1).build())
                .employeestatus(EmployeeStatusDto.builder().id(1).build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingFieldProvider() {
        return Stream.of(
                //Arguments.of("number", "Number is mandatory", (Consumer<EmployeeCreateRequestDto>) dto -> dto.setNumber(null)),
                Arguments.of("fullname", "Full name is mandatory",(Consumer<EmployeeCreateRequestDto>) dto -> dto.setFullname(null)),
                Arguments.of("callingname", "Calling name is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setCallingname(null)),
                Arguments.of("nic", "NIC is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setNic(null)),
                Arguments.of("gender", "Gender is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setGender(null)),
                Arguments.of("mobile", "Mobile number is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setMobile(null)),
                //Arguments.of("email", "Email is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setEmail(null)),
                Arguments.of("address", "Address is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setAddress(null)),
                Arguments.of("emergencycontact", "Emergency contact is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setEmergencycontact(null)),
                Arguments.of("doj", "Date of joining is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setDoj(null)),
                Arguments.of("branch", "Branch is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setBranch(null)),
                Arguments.of("department", "Department is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setDepartment(null)),
                Arguments.of("designation", "Designation is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setDesignation(null)),
                Arguments.of("employeetype", "Employee type is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setEmployeetype(null)),
                Arguments.of("employeestatus", "Employee status is mandatory", (Consumer<EmployeeCreateRequestDto>) dto  -> dto.setEmployeestatus(null))
        );
    }

//    @ParameterizedTest
//    @ValueSource(strings = {
//            "",
//            "EMP@001",
//            "123",
//            "EMPLOYEE_NUMBER_TOO_LONG_12345"
//    })
//    void createEmployee_shouldFail_whenNumberIsInvalid(
//            String invalidNumber
//    ) throws Exception {
//
//        EmployeeCreateRequestDto dto = EmployeeCreateRequestDto.builder()
//                //.number("EMP0001")
//                .fullname("John Silva")
//                .callingname("John")
//                .nic("200012345678")
//                .gender(GenderDto.builder().id(1).build())
//                .mobile("0712345678")
//                //.email("john@sltb.lk")
//                .address("Colombo")
//                .emergencycontact("0771234567")
//                .doj(LocalDate.now().minusDays(10))
//                .branch(BranchSummaryDto.builder().id(1).build())
//                .department(DepartmentDto.builder().id(1).build())
//                .designation(DesignationDto.builder().id(1).build())
//                .employeetype(EmployeeTypeDto.builder().id(1).build())
//                .employeestatus(EmployeeStatusDto.builder().id(1).build())
//                .build();
//        dto.setNumber(invalidNumber);
//
//        mockMvc.perform(post(API_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.details",
//                        hasItem(containsString("Invalid employee number"))));
//    }

    @ParameterizedTest
    @ValueSource(strings = {
            "071234567",
            "07123456789",
            "07123A5678",
            " 0712345678",
            "0712345678 "
    })
    void createEmployee_shouldFail_whenMobileIsInvalid(
            String invalidMobile
    ) throws Exception {

        EmployeeCreateRequestDto dto = EmployeeCreateRequestDto.builder()
               // .number("EMP0001")
                .fullname("John Silva")
                .callingname("John")
                .nic("200012345678")
                .gender(GenderDto.builder().id(1).build())
                .mobile("0712345678")
               // .email("john@sltb.lk")
                .address("Colombo")
                .emergencycontact("0771234567")
                .doj(LocalDate.now().minusDays(10))
                .branch(BranchSummaryDto.builder().id(1).build())
                .department(DepartmentDto.builder().id(1).build())
                .designation(DesignationDto.builder().id(1).build())
                .employeetype(EmployeeTypeDto.builder().id(1).build())
                .employeestatus(EmployeeStatusDto.builder().id(1).build())
                .build();

        dto.setMobile(invalidMobile);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(containsString("Invalid mobile number"))));
    }



    @ParameterizedTest
    @MethodSource("dojTestCases")
    @Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/employee/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/modules/employee/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createEmployee_dojValidation_shouldWork(
            LocalDate inputDate,
            ResultMatcher expectedStatus,
            String expectedError
    ) throws Exception {

        EmployeeCreateRequestDto dto = EmployeeCreateRequestDto.builder()
                //.number("EMP0007")
                .fullname("John Silva")
                .callingname("John")
                .nic("200312345678")
                .gender(GenderDto.builder().id(1).name("Male").build())
                .mobile("0712342678")
                //.email("john@sltb.lk")
                .address("Colombo")
                .emergencycontact("0761234567")
                .doj(inputDate)
                .branch(BranchSummaryDto.builder().id(1).build())
                .department(DepartmentDto.builder().id(1).name("Operations").build())
                .designation(DesignationDto.builder().id(1).name("Conductor").build())
                .employeetype(EmployeeTypeDto.builder().id(1).name("Probation").build())
                .employeestatus(EmployeeStatusDto.builder().id(1).name("Active").build())
                .build();

        ResultActions resultActions =
                mockMvc.perform(post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(expectedStatus);

        if (expectedError != null) {
            resultActions.andExpect(
                    jsonPath("$.details",
                            hasItem(containsString(expectedError)))
            );
        }
    }

    private static Stream<Arguments> dojTestCases() {
        return Stream.of(
                Arguments.of(
                        LocalDate.now().plusDays(1),
                        status().isBadRequest(),
                        "Date of joining cannot be in the future"
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
    @Sql(
            scripts = "/modules/employee/schema.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "/modules/employee/data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void createEmployee_shouldFail_whenDuplicateField(
            String expectedError,
            Consumer<EmployeeCreateRequestDto> mutator
    ) throws Exception {

        EmployeeCreateRequestDto dto = EmployeeCreateRequestDto.builder()
                //.number("EMP0007")
                .fullname("John Silva")
                .callingname("John")
                .nic("200012945678")
                .gender(GenderDto.builder().id(1).name("Male").build())
                .mobile("0714345678")
                //.email("john@sltb.lk")
                .address("Colombo")
                .emergencycontact("0771234561")
                .doj(LocalDate.now().minusDays(100))
                .branch(BranchSummaryDto.builder().id(1).build())
                .department(DepartmentDto.builder().id(1).name("Operations").build())
                .designation(DesignationDto.builder().id(1).name("Conductor").build())
                .employeetype(EmployeeTypeDto.builder().id(1).name("Probation").build())
                .employeestatus(EmployeeStatusDto.builder().id(1).name("Active").build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details")
                        .value(expectedError));
    }

    private static Stream<Arguments> duplicateFieldProvider() {
        return Stream.of(

//                Arguments.of(
//                        "Employee number already exists.",
//                        (Consumer<EmployeeCreateRequestDto>) dto -> dto.setNumber("EMP0001")
//                ),

                Arguments.of(
                        "NIC already exists.",
                        (Consumer<EmployeeCreateRequestDto>) dto -> dto.setNic("200012345678")
                ),

                Arguments.of(
                        "Mobile number already exists.",
                        (Consumer<EmployeeCreateRequestDto>) dto -> dto.setMobile("0712345678")
                ),

//                Arguments.of(
//                        "Email already exists.",
//                        (Consumer<EmployeeCreateRequestDto>) dto -> dto.setEmail("sunil.emp0001@sltb.lk")
//                ),

                Arguments.of(
                        "Emergency contact already exists.",
                        (Consumer<EmployeeCreateRequestDto>) dto -> dto.setEmergencycontact("0771234567")
                ),

                Arguments.of(
                        "Mobile number already used as emergency contact by another employee.",
                        (Consumer<EmployeeCreateRequestDto>) dto -> dto.setMobile("0771234567")
                ),

                Arguments.of(
                        "Emergency contact already used as another employee’s mobile number.",
                        (Consumer<EmployeeCreateRequestDto>) dto -> dto.setEmergencycontact("0712345678")
                )
        );
    }


//    @Test
//    @Sql(
//            scripts = "/modules/branch/schema.sql",
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
//    )
//    @Sql(
//            scripts = "/modules/branch/data.sql",
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
//    )
//    @Sql(
//            scripts = "/modules/employee/schema.sql",
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
//    )
//    @Sql(
//            scripts = "/modules/employee/data.sql",
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
//    )
//    void updateEmployee_shouldFail_whenNumberIsChanged() throws Exception {
//        EmployeeUpdateRequestDto dto =EmployeeUpdateRequestDto.builder()
//                .id(1)
//                //.number("EMP0001")
//                .fullname("Sunil Perera")
//                .callingname("Sunil")
//                .nic("200012345678")
//                .gender(GenderDto.builder().id(1).build())
//                .mobile("0712345678")
//                //.email("sunil.emp0001@sltb.lk")
//                .address("No 12,kandy Rd,Colombo")
//                .emergencycontact("0771234567")
//                .doj(LocalDate.parse("2015-03-12"))
//                .branch(BranchSummaryDto.builder().id(1).name("Colombo head office").build())
//                .department(DepartmentDto.builder().id(1).name("Operations").build())
//                .designation(DesignationDto.builder().id(1).name("Driver").build())
//                .employeetype(EmployeeTypeDto.builder().id(1).name("Permanent").build())
//                .employeestatus(EmployeeStatusDto.builder().id(1).name("Active").build())
//                .build();
//
//        //dto.setNumber("EMP9999");
//
//        mockMvc.perform(put(API_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isConflict())
//                .andExpect(jsonPath("$.details",
//                        hasItem("Employee number cannot be changed")));
//    }




    @Test
    @Sql(
            scripts = "/modules/branch/schema.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "/modules/branch/data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "/modules/employee/schema.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "/modules/employee/data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void updateEmployee_shouldSucceed_whenSameValuesUsed()
            throws Exception {

                EmployeeUpdateRequestDto dto =EmployeeUpdateRequestDto.builder()
                .id(1)
                //.number("EMP0001")
                .fullname("Sunil Perera")
                .callingname("Sunil")
                .nic("200012345678")
                .gender(GenderDto.builder().id(1).build())
                .mobile("0712345678")
                //.email("sunil.emp0001@sltb.lk")
                .address("No 12,kandy Rd,Colombo")
                .emergencycontact("0771234567")
                .gender(GenderDto.builder().id(1).name("Male").build())
                .doj(LocalDate.parse("2015-03-12"))
                .branch(BranchSummaryDto.builder().id(1).name("Colombo head office").build())
                .department(DepartmentDto.builder().id(1).name("Operations").build())
                .designation(DesignationDto.builder().id(1).name("Driver").build())
                .employeetype(EmployeeTypeDto.builder().id(1).name("Permanent").build())
                .employeestatus(EmployeeStatusDto.builder().id(1).name("Active").build())
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
/*
    // =========================================================
    // STATUS TRANSITION
    // =========================================================

    @Test
    void updateEmployee_shouldSucceed_whenStatusChanged()
            throws Exception {

        EmployeeUpdateRequestDto dto =
                buildValidEmployeeUpdate();

        dto.setEmployeestatus(
                EmployeeStatusDto.builder()
                        .id(2)
                        .name("Inactive")
                        .build()
        );

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
**/
    // =========================================================
    // COMMON DTO BUILDERS
    // =========================================================


}
