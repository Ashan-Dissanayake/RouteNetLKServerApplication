package lk.ashan.routenetlkserverapllication.module.privilege.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.ModuleDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.OperationDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeAssignRequestDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeRequestDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeResponseDto;
import lk.ashan.routenetlkserverapllication.module.privilege.service.PrivilegeService;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrivilegeController.class)
@Import(TestSecurityConfiguration.class)
class PrivilegeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PrivilegeService privilegeService;

    private static final String API_URL = "/privileges";


    // =========================================================
    // GET /privileges
    // =========================================================

    @Test
    void getPrivileges_shouldReturn200_whenAuthorized() throws Exception {

        PrivilegeResponseDto response = PrivilegeResponseDto.builder()
                .id(1)
                .authority("employee-view")
                .module(
                        ModuleDto.builder()
                                .id(1)
                                .name("Employee")
                                .build()
                )
                .operation(
                        OperationDto.builder()
                                .id(1)
                                .operation("view")
                                .displayname("View")
                                .build()
                )
                .build();

        when(privilegeService.getPrivileges())
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "privilege-view")))
                .andExpect(status().isOk());

        verify(privilegeService).getPrivileges();
    }


    @Test
    void getPrivileges_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(get(API_URL)
                        .with(user("test-user")
                                .authorities(() -> "privilege-assign")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(privilegeService);
    }


    @Test
    void getPrivileges_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(get(API_URL))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(privilegeService);
    }


    @Test
    void searchPrivileges_shouldCallSearchService_whenQueryParamsProvided() throws Exception {

        PrivilegeResponseDto response = PrivilegeResponseDto.builder()
                .id(1)
                .authority("employee-view")
                .build();

        when(privilegeService.searchPrivileges(any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get(API_URL)
                        .param("authority", "employee-view")
                        .with(user("test-user")
                                .authorities(() -> "privilege-view")))
                .andExpect(status().isOk());

        verify(privilegeService).searchPrivileges(any());
        verify(privilegeService, never()).getPrivileges();
    }


    // =========================================================
    // POST /privileges/{roleId}/assign
    // =========================================================

    @Test
    void assignPrivileges_shouldReturn200_whenRequestIsValid() throws Exception {

        PrivilegeAssignRequestDto dto = validAssignRequest();

        mockMvc.perform(post(API_URL + "/1/assign")
                        .with(user("test-user")
                                .authorities(() -> "privilege-assign"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(privilegeService)
                .assignPrivileges(
                        anyInt(),
                        any(PrivilegeAssignRequestDto.class)
                );
    }


    @Test
    void assignPrivileges_shouldReturn400_whenPrivilegesAreEmpty() throws Exception {

        PrivilegeAssignRequestDto dto = PrivilegeAssignRequestDto.builder()
                .privileges(List.of())
                .build();

        mockMvc.perform(post(API_URL + "/1/assign")
                        .with(user("test-user")
                                .authorities(() -> "privilege-assign"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(
                        "$.details",
                        hasItem("privileges: At least one privilege must be selected")
                ));

        verifyNoInteractions(privilegeService);
    }


    @Test
    void assignPrivileges_shouldReturn400_whenPrivilegesAreNull() throws Exception {

        PrivilegeAssignRequestDto dto = PrivilegeAssignRequestDto.builder()
                .privileges(null)
                .build();

        mockMvc.perform(post(API_URL + "/1/assign")
                        .with(user("test-user")
                                .authorities(() -> "privilege-assign"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(
                        "$.details",
                        hasItem("privileges: At least one privilege must be selected")
                ));

        verifyNoInteractions(privilegeService);
    }


    @Test
    void assignPrivileges_shouldReturn400_whenModuleIsMissing() throws Exception {

        PrivilegeAssignRequestDto dto =
                PrivilegeAssignRequestDto.builder()
                        .privileges(List.of(
                                PrivilegeRequestDto.builder()
                                        .module(null)
                                        .operation(validOperation())
                                        .build()
                        ))
                        .build();

        mockMvc.perform(post(API_URL + "/1/assign")
                        .with(user("test-user")
                                .authorities(() -> "privilege-assign"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem("privileges[0].module: Module is mandatory")));

        verifyNoInteractions(privilegeService);
    }


    @Test
    void assignPrivileges_shouldReturn400_whenOperationIsMissing() throws Exception {

        PrivilegeAssignRequestDto dto =
                PrivilegeAssignRequestDto.builder()
                        .privileges(List.of(
                                PrivilegeRequestDto.builder()
                                        .module(validModule())
                                        .operation(null)
                                        .build()
                        ))
                        .build();

        mockMvc.perform(post(API_URL + "/1/assign")
                        .with(user("test-user")
                                .authorities(() -> "privilege-assign"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem("privileges[0].operation: Operation is mandatory")));

        verifyNoInteractions(privilegeService);
    }

    @Test
    void assignPrivileges_shouldReturn403_whenWrongAuthority() throws Exception {

        PrivilegeAssignRequestDto dto = validAssignRequest();

        mockMvc.perform(post(API_URL + "/1/assign")
                        .with(user("test-user")
                                .authorities(() -> "privilege-view"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(privilegeService);
    }


    @Test
    void assignPrivileges_shouldReturn401_whenAnonymous() throws Exception {

        PrivilegeAssignRequestDto dto = validAssignRequest();

        mockMvc.perform(post(API_URL + "/1/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(privilegeService);
    }


    // =========================================================
    // DELETE /privileges/{roleId}/{privilegeId}/revoke
    // =========================================================

    @Test
    void removePrivilege_shouldReturn200_whenAuthorized() throws Exception {

        mockMvc.perform(delete(API_URL + "/1/10/revoke")
                        .with(user("test-user")
                                .authorities(() -> "privilege-revoke")))
                .andExpect(status().isOk());

        verify(privilegeService)
                .removePrivilege(1, 10);
    }


    @Test
    void removePrivilege_shouldReturn403_whenWrongAuthority() throws Exception {

        mockMvc.perform(delete(API_URL + "/1/10/revoke")
                        .with(user("test-user")
                                .authorities(() -> "privilege-view")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(privilegeService);
    }


    @Test
    void removePrivilege_shouldReturn401_whenAnonymous() throws Exception {

        mockMvc.perform(delete(API_URL + "/1/10/revoke"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(privilegeService);
    }


    // =========================================================
    // Test Data
    // =========================================================

    private PrivilegeAssignRequestDto validAssignRequest() {

        return PrivilegeAssignRequestDto.builder()
                .privileges(List.of(
                        PrivilegeRequestDto.builder()
                                .module(validModule())
                                .operation(validOperation())
                                .build()
                ))
                .build();
    }


    private ModuleDto validModule() {

        return ModuleDto.builder()
                .id(1)
                .name("Employee")
                .build();
    }


    private OperationDto validOperation() {

        return OperationDto.builder()
                .id(1)
                .displayname("View")
                .operation("view")
                .module(validModule())
                .build();
    }
}
