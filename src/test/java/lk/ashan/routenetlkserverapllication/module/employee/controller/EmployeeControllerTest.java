package lk.ashan.routenetlkserverapllication.module.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeeService;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(TestSecurityConfiguration.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    private static final String API_URL = "/employees";


    // =========================================================
    // GET /employees
    // =========================================================

    @Test
    void getEmployees_shouldReturn200_whenAuthorized() throws Exception {

        EmployeeDetailResponseDto response = EmployeeDetailResponseDto.builder()
                .id(1)
                .number("EMP001")
                .fullname("Sunil Perera")
                .callingname("Sunil")
                .nic("901234567V")
                .mobile("0712345678")
                .build();

        when(employeeService.getEmployees())
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-view"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(employeeService).getEmployees();
    }


    @Test
    void getEmployees_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(
                        get(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(employeeService);
    }


    @Test
    void getEmployees_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(employeeService);
    }


    @Test
    void searchEmployees_shouldCallSearchService_whenQueryParamsProvided()
            throws Exception {

        EmployeeDetailResponseDto response = EmployeeDetailResponseDto.builder()
                .id(1)
                .number("EMP001")
                .fullname("Sunil Perera")
                .build();

        when(employeeService.searchEmployee(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get(API_URL)
                                .param("fullname", "Sunil Perera")
                                .with(user("test-user")
                                        .authorities(() -> "employee-view"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(employeeService).searchEmployee(any());
        verify(employeeService, never()).getEmployees();
    }


    // =========================================================
    // GET /employees/summaries
    // =========================================================

    @Test
    void getEmployeeSummaries_shouldReturn200_whenAuthenticated()
            throws Exception {

        when(employeeService.getSummaryEmployees())
                .thenReturn(List.of(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .callingname("Sunil")
                                .build()
                ));

        mockMvc.perform(
                        get(API_URL + "/summaries")
                                .with(user("test-user"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(employeeService).getSummaryEmployees();
    }


    @Test
    void getEmployeeSummaries_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(get(API_URL + "/summaries"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(employeeService);
    }


    // =========================================================
    // GET /employees/summaries/{designation}
    // =========================================================

    @Test
    void getEmployeesByDesignation_shouldReturn200_whenAuthenticated()
            throws Exception {

        when(employeeService.getEmployeesByDesignation("driver"))
                .thenReturn(List.of(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .callingname("Sunil")
                                .build()
                ));

        mockMvc.perform(
                        get(API_URL + "/summaries/driver")
                                .with(user("test-user"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(employeeService)
                .getEmployeesByDesignation("driver");
    }


    @Test
    void getEmployeesByDesignation_shouldReturn401_whenAnonymous()
            throws Exception {

        mockMvc.perform(
                        get(API_URL + "/summaries/driver")
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(employeeService);
    }


    // =========================================================
    // POST /employees
    // =========================================================

    @Test
    void createEmployee_shouldReturn201_whenRequestIsValid()
            throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();

        EmployeeDetailResponseDto response =
                EmployeeDetailResponseDto.builder()
                        .id(1)
                        .number("EMP001")
                        .fullname(dto.getFullname())
                        .callingname(dto.getCallingname())
                        .nic(dto.getNic())
                        .mobile(dto.getMobile())
                        .build();

        when(employeeService.createEmployee(
                any(EmployeeCreateRequestDto.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isCreated());

        verify(employeeService)
                .createEmployee(any(EmployeeCreateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingCreateFieldProvider")
    void createEmployee_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<EmployeeCreateRequestDto> mutator
    ) throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();

        mutator.accept(dto);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                field + ": " + errorMessage
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<Arguments> missingCreateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "fullname",
                        "Full name is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setFullname(null)
                ),

                Arguments.of(
                        "callingname",
                        "Calling name is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setCallingname(null)
                ),

                Arguments.of(
                        "nic",
                        "NIC is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setNic(null)
                ),

                Arguments.of(
                        "gender",
                        "Gender is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setGender(null)
                ),

                Arguments.of(
                        "mobile",
                        "Mobile number is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setMobile(null)
                ),

                Arguments.of(
                        "address",
                        "Address is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setAddress(null)
                ),

                Arguments.of(
                        "emergencycontact",
                        "Emergency contact is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setEmergencycontact(null)
                ),

                Arguments.of(
                        "doj",
                        "Date of joining is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setDoj(null)
                ),

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "department",
                        "Department is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setDepartment(null)
                ),

                Arguments.of(
                        "designation",
                        "Designation is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setDesignation(null)
                ),

                Arguments.of(
                        "employeetype",
                        "Employee type is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setEmployeetype(null)
                ),

                Arguments.of(
                        "employeestatus",
                        "Employee status is mandatory",
                        (Consumer<EmployeeCreateRequestDto>)
                                dto -> dto.setEmployeestatus(null)
                )
        );
    }


    // =========================================================
    // Employee Create Format Validations
    // =========================================================

    @ParameterizedTest
    @MethodSource("invalidCreateFullNameProvider")
    void createEmployee_shouldReturn400_whenFullNameIsInvalid(
            String invalidFullName
    ) throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();
        dto.setFullname(invalidFullName);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString("Invalid full name")
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<String> invalidCreateFullNameProvider() {

        return Stream.of(
                "",
                "sunil Perera",
                "Sunil perera",
                "Sunil123",
                "Sunil@Perera"
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCreateCallingNameProvider")
    void createEmployee_shouldReturn400_whenCallingNameIsInvalid(
            String invalidCallingName
    ) throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();
        dto.setCallingname(invalidCallingName);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString("Invalid calling name")
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<String> invalidCreateCallingNameProvider() {

        return Stream.of(
                "",
                "sunil",
                "Sunil123",
                "Sunil Perera",
                "Sunil@"
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCreateNicProvider")
    void createEmployee_shouldReturn400_whenNicIsInvalid(
            String invalidNic
    ) throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();
        dto.setNic(invalidNic);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString("Invalid NIC")
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<String> invalidCreateNicProvider() {

        return Stream.of(
                "12345678",
                "123456789X",
                "1234567890V",
                "1234567890123",
                "ABC123456V"
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCreateMobileProvider")
    void createEmployee_shouldReturn400_whenMobileIsInvalid(
            String invalidMobile
    ) throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();
        dto.setMobile(invalidMobile);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString("Invalid mobile number")
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<String> invalidCreateMobileProvider() {

        return Stream.of(
                "071234567",
                "07123456789",
                "0812345678",
                "0792345678",
                "071234567A",
                "071 2345678",
                "+94712345678"
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCreateAddressProvider")
    void createEmployee_shouldReturn400_whenAddressIsInvalid(
            String invalidAddress
    ) throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();
        dto.setAddress(invalidAddress);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString("Invalid address")
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<String> invalidCreateAddressProvider() {

        return Stream.of(
                "",
                "A",
                "@@@",
                "Address#123"
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCreateEmergencyContactProvider")
    void createEmployee_shouldReturn400_whenEmergencyContactIsInvalid(
            String invalidEmergencyContact
    ) throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();
        dto.setEmergencycontact(invalidEmergencyContact);

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString("Invalid emergency contact")
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<String> invalidCreateEmergencyContactProvider() {

        return Stream.of(
                "071234567",
                "07123456789",
                "0792345678",
                "071234567A",
                "+94712345678"
        );
    }


    @Test
    void createEmployee_shouldReturn400_whenDateOfJoiningIsInFuture()
            throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();
        dto.setDoj(LocalDate.now().plusDays(1));

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                containsString(
                                        "Date of joining cannot be in the future"
                                )
                        )));

        verifyNoInteractions(employeeService);
    }


    // =========================================================
    // POST Security
    // =========================================================

    @Test
    void createEmployee_shouldReturn403_whenWrongAuthority()
            throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(
                        post(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(employeeService);
    }


    @Test
    void createEmployee_shouldReturn401_whenAnonymous()
            throws Exception {

        EmployeeCreateRequestDto dto = validCreateRequest();

        mockMvc.perform(
                        post(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(employeeService);
    }


    // =========================================================
    // PUT /employees
    // =========================================================

    @Test
    void updateEmployee_shouldReturn200_whenRequestIsValid()
            throws Exception {

        EmployeeUpdateRequestDto dto = validUpdateRequest();

        EmployeeDetailResponseDto response =
                EmployeeDetailResponseDto.builder()
                        .id(1)
                        .number("EMP001")
                        .fullname(dto.getFullname())
                        .callingname(dto.getCallingname())
                        .nic(dto.getNic())
                        .mobile(dto.getMobile())
                        .build();

        when(employeeService.updateEmployee(
                any(EmployeeUpdateRequestDto.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isOk());

        verify(employeeService)
                .updateEmployee(any(EmployeeUpdateRequestDto.class));
    }


    @ParameterizedTest
    @MethodSource("missingUpdateFieldProvider")
    void updateEmployee_shouldReturn400_whenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<EmployeeUpdateRequestDto> mutator
    ) throws Exception {

        EmployeeUpdateRequestDto dto = validUpdateRequest();

        mutator.accept(dto);

        mockMvc.perform(
                        put(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                field + ": " + errorMessage
                        )));

        verifyNoInteractions(employeeService);
    }


    static Stream<Arguments> missingUpdateFieldProvider() {

        return Stream.of(

                Arguments.of(
                        "id",
                        "must not be null",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setId(null)
                ),

                Arguments.of(
                        "fullname",
                        "Full name is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setFullname(null)
                ),

                Arguments.of(
                        "callingname",
                        "Calling name is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setCallingname(null)
                ),

                Arguments.of(
                        "nic",
                        "NIC is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setNic(null)
                ),

                Arguments.of(
                        "gender",
                        "Gender is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setGender(null)
                ),

                Arguments.of(
                        "mobile",
                        "Mobile number is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setMobile(null)
                ),

                Arguments.of(
                        "address",
                        "Address is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setAddress(null)
                ),

                Arguments.of(
                        "emergencycontact",
                        "Emergency contact is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setEmergencycontact(null)
                ),

                Arguments.of(
                        "doj",
                        "Date of joining is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setDoj(null)
                ),

                Arguments.of(
                        "branch",
                        "Branch is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "department",
                        "Department is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setDepartment(null)
                ),

                Arguments.of(
                        "designation",
                        "Designation is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setDesignation(null)
                ),

                Arguments.of(
                        "employeetype",
                        "Employee type is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setEmployeetype(null)
                ),

                Arguments.of(
                        "employeestatus",
                        "Employee status is mandatory",
                        (Consumer<EmployeeUpdateRequestDto>)
                                dto -> dto.setEmployeestatus(null)
                )
        );
    }


    // =========================================================
    // PUT Security
    // =========================================================

    @Test
    void updateEmployee_shouldReturn403_whenWrongAuthority()
            throws Exception {

        EmployeeUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(
                        put(API_URL)
                                .with(user("test-user")
                                        .authorities(() -> "employee-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(employeeService);
    }


    @Test
    void updateEmployee_shouldReturn401_whenAnonymous()
            throws Exception {

        EmployeeUpdateRequestDto dto = validUpdateRequest();

        mockMvc.perform(
                        put(API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(dto)
                                )
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(employeeService);
    }


    // =========================================================
    // DELETE /employees/deactivate
    // =========================================================

    @Test
    void deactivateEmployees_shouldReturn200_whenAuthorized()
            throws Exception {

        List<Integer> ids = List.of(1, 2, 3);

        when(employeeService.deactivateEmployee(anyList()))
                .thenReturn(ids);

        mockMvc.perform(
                        delete(API_URL + "/deactivate")
                                .with(user("test-user")
                                        .authorities(() -> "employee-delete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(ids)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.status")
                        .value("deactivated"))
                .andExpect(jsonPath("$.meta.count")
                        .value(3));

        verify(employeeService).deactivateEmployee(anyList());
    }


    @Test
    void deactivateEmployees_shouldReturn403_whenWrongAuthority()
            throws Exception {

        List<Integer> ids = List.of(1, 2, 3);

        mockMvc.perform(
                        delete(API_URL + "/deactivate")
                                .with(user("test-user")
                                        .authorities(() -> "employee-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(ids)
                                )
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(employeeService);
    }


    @Test
    void deactivateEmployees_shouldReturn401_whenAnonymous()
            throws Exception {

        List<Integer> ids = List.of(1, 2, 3);

        mockMvc.perform(
                        delete(API_URL + "/deactivate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(ids)
                                )
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(employeeService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private EmployeeCreateRequestDto validCreateRequest() {

        return EmployeeCreateRequestDto.builder()
                .fullname("Sunil Perera")
                .callingname("Sunil")
                .nic("901234567V")
                .gender(
                        GenderDto.builder()
                                .id(1)
                                .build()
                )
                .mobile("0712345678")
                .address("Kandy Road, Dambulla")
                .emergencycontact("0771234567")
                .doj(LocalDate.now().minusYears(2))
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .department(
                        DepartmentDto.builder()
                                .id(1)
                                .build()
                )
                .designation(
                        DesignationDto.builder()
                                .id(1)
                                .build()
                )
                .employeetype(
                        EmployeeTypeDto.builder()
                                .id(1)
                                .build()
                )
                .employeestatus(
                        EmployeeStatusDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }


    private EmployeeUpdateRequestDto validUpdateRequest() {

        return EmployeeUpdateRequestDto.builder()
                .id(1)
                .fullname("Sunil Perera")
                .callingname("Sunil")
                .nic("901234567V")
                .gender(
                        GenderDto.builder()
                                .id(1)
                                .build()
                )
                .mobile("0712345678")
                .address("Kandy Road, Dambulla")
                .emergencycontact("0771234567")
                .doj(LocalDate.now().minusYears(2))
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .department(
                        DepartmentDto.builder()
                                .id(1)
                                .build()
                )
                .designation(
                        DesignationDto.builder()
                                .id(1)
                                .build()
                )
                .employeetype(
                        EmployeeTypeDto.builder()
                                .id(1)
                                .build()
                )
                .employeestatus(
                        EmployeeStatusDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }
}
