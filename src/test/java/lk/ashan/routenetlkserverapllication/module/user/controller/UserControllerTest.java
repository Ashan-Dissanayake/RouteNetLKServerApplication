package lk.ashan.routenetlkserverapllication.module.user.controller;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.user.service.UserService;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(UserController.class)
@Import(TestSecurityConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    // ==================== GET /users ====================

    @Test
    void getUsers_ShouldReturn200() throws Exception {

        when(userService.getUsers())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/users")
                                .with(authority("user-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(userService).getUsers();
    }

    @Test
    void getUsers_ShouldReturn403_WhenUserDoesNotHaveUserViewAuthority()
            throws Exception {

        mockMvc.perform(
                        get("/users")
                                .with(authority("vehicle-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void getUsers_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        get("/users")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }


    // ==================== POST /users ====================

    @Test
    void createUser_ShouldReturn201_WhenRequestIsValid()
            throws Exception {

        UserCreateRequestDto request = validCreateRequest();

        when(userService.createUser(any(UserCreateRequestDto.class)))
                .thenReturn(userResponse());

        mockMvc.perform(
                        post("/users")
                                .with(authority("user-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.meta.status").value("created"));

        verify(userService)
                .createUser(any(UserCreateRequestDto.class));
    }

    @Test
    void createUser_ShouldReturn403_WhenUserLacksUserAddAuthority()
            throws Exception {

        UserCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post("/users")
                                .with(authority("user-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void createUser_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        UserCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }

    @ParameterizedTest
    @MethodSource("createUserMissingFieldProvider")
    void createUser_ShouldReturn400_WhenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<UserCreateRequestDto> mutator) throws Exception {

        UserCreateRequestDto request = validCreateRequest();

        mutator.accept(request);

        mockMvc.perform(
                        post("/users")
                                .with(authority("user-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(userService);
    }

    static Stream<Arguments> createUserMissingFieldProvider() {

        return Stream.of(
                Arguments.of(
                        "employee",
                        "Employee is mandatory",
                        (Consumer<UserCreateRequestDto>)
                                dto -> dto.setEmployee(null)
                ),

                Arguments.of(
                        "username",
                        "Username is mandatory",
                        (Consumer<UserCreateRequestDto>)
                                dto -> dto.setUsername(null)
                ),

                Arguments.of(
                        "password",
                        "Password is mandatory",
                        (Consumer<UserCreateRequestDto>)
                                dto -> dto.setPassword(null)
                ),

                Arguments.of(
                        "usertype",
                        "User Type is mandatory",
                        (Consumer<UserCreateRequestDto>)
                                dto -> dto.setUsertype(null)
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "user@name",
            "user name",
            "user#name",
            "user/name"
    })
    void createUser_ShouldReturn400_WhenUsernameIsInvalid(
            String invalidUsername) throws Exception {

        UserCreateRequestDto request = validCreateRequest();
        request.setUsername(invalidUsername);

        mockMvc.perform(
                        post("/users")
                                .with(authority("user-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("username: Invalid Username")));

        verifyNoInteractions(userService);
    }

    @Test
    void createUser_ShouldReturn400_WhenUsernameExceedsMaximumLength()
            throws Exception {

        UserCreateRequestDto request = validCreateRequest();

        request.setUsername(
                "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy"
        );

        mockMvc.perform(
                        post("/users")
                                .with(authority("user-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "password",
            "PASSWORD",
            "password123",
            "PASSWORD123"
    })
    void createUser_ShouldReturn400_WhenPasswordIsInvalid(
            String invalidPassword) throws Exception {

        UserCreateRequestDto request = validCreateRequest();
        request.setPassword(invalidPassword);

        mockMvc.perform(
                        post("/users")
                                .with(authority("user-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("password: Invalid Password")));

        verifyNoInteractions(userService);
    }


    // ==================== PUT /users ====================

    @Test
    void updateUser_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        UserUpdateRequestDto request = validUpdateRequest();

        when(userService.updateUser(any(UserUpdateRequestDto.class)))
                .thenReturn(userResponse());

        mockMvc.perform(
                        put("/users")
                                .with(authority("user-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.meta.status").value("updated"));

        verify(userService)
                .updateUser(any(UserUpdateRequestDto.class));
    }

    @Test
    void updateUser_ShouldReturn400_WhenRequestIsInvalid()
            throws Exception {

        UserUpdateRequestDto request = validUpdateRequest();
        request.setUsername(null);

        mockMvc.perform(
                        put("/users")
                                .with(authority("user-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("username: Username is mandatory")));

        verifyNoInteractions(userService);
    }

    @Test
    void updateUser_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        UserUpdateRequestDto request = validUpdateRequest();

        mockMvc.perform(
                        put("/users")
                                .with(authority("user-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void updateUser_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        UserUpdateRequestDto request = validUpdateRequest();

        mockMvc.perform(
                        put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }


    // ==================== PUT /users/activate-or-deactivate-user ====================

    @Test
    void activateOrDeactivateUser_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        UserActiveDeactiveDto request = validActiveDeactiveRequest();

        doNothing()
                .when(userService)
                .activateOrDeactivateUser(
                        any(UserActiveDeactiveDto.class)
                );

        mockMvc.perform(
                        put("/users/activate-or-deactivate-user")
                                .with(authority("user-delete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(userService)
                .activateOrDeactivateUser(
                        any(UserActiveDeactiveDto.class)
                );
    }

    @Test
    void activateOrDeactivateUser_ShouldReturn400_WhenRequestIsInvalid()
            throws Exception {

        UserActiveDeactiveDto request = validActiveDeactiveRequest();
        request.setUsername(null);

        mockMvc.perform(
                        put("/users/activate-or-deactivate-user")
                                .with(authority("user-delete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("username: Username is mandatory")));

        verifyNoInteractions(userService);
    }

    @Test
    void activateOrDeactivateUser_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        UserActiveDeactiveDto request = validActiveDeactiveRequest();

        mockMvc.perform(
                        put("/users/activate-or-deactivate-user")
                                .with(authority("user-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void activateOrDeactivateUser_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        UserActiveDeactiveDto request = validActiveDeactiveRequest();

        mockMvc.perform(
                        put("/users/activate-or-deactivate-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }


    // ==================== PUT /users/{userId}/change-password ====================

    @Test
    void changePassword_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        ChangePasswordRequestDto request = validChangePasswordRequest();

        doNothing()
                .when(userService)
                .changePassword(
                        eq(1),
                        any(ChangePasswordRequestDto.class)
                );

        mockMvc.perform(
                        put("/users/1/change-password")
                                .with(authority("user-change-password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(userService)
                .changePassword(
                        eq(1),
                        any(ChangePasswordRequestDto.class)
                );
    }

    @Test
    void changePassword_ShouldReturn400_WhenRequestIsInvalid()
            throws Exception {

        ChangePasswordRequestDto request = validChangePasswordRequest();
        request.setCurrentPassword(null);

        mockMvc.perform(
                        put("/users/1/change-password")
                                .with(authority("user-change-password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "currentPassword: Current password is mandatory"
                        )));

        verifyNoInteractions(userService);
    }

    @Test
    void changePassword_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        ChangePasswordRequestDto request = validChangePasswordRequest();

        mockMvc.perform(
                        put("/users/1/change-password")
                                .with(authority("user-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void changePassword_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        ChangePasswordRequestDto request = validChangePasswordRequest();

        mockMvc.perform(
                        put("/users/1/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "password",
            "PASSWORD",
            "password123",
            "PASSWORD123"
    })
    void changePassword_ShouldReturn400_WhenNewPasswordIsInvalid(
            String invalidPassword) throws Exception {

        ChangePasswordRequestDto request = validChangePasswordRequest();
        request.setNewPassword(invalidPassword);

        mockMvc.perform(
                        put("/users/1/change-password")
                                .with(authority("user-change-password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("newPassword: Invalid Password")));

        verifyNoInteractions(userService);
    }


    // ==================== PUT /users/{userId}/reset-password ====================

    @Test
    void resetPassword_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        ResetPasswordRequestDto request = validResetPasswordRequest();

        doNothing()
                .when(userService)
                .resetPassword(
                        eq(1),
                        any(ResetPasswordRequestDto.class)
                );

        mockMvc.perform(
                        put("/users/1/reset-password")
                                .with(authority("user-reset-password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(userService)
                .resetPassword(
                        eq(1),
                        any(ResetPasswordRequestDto.class)
                );
    }

    @Test
    void resetPassword_ShouldReturn400_WhenRequestIsInvalid()
            throws Exception {

        ResetPasswordRequestDto request = validResetPasswordRequest();
        request.setNewPassword(null);

        mockMvc.perform(
                        put("/users/1/reset-password")
                                .with(authority("user-reset-password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "newPassword: New password is mandatory"
                        )));

        verifyNoInteractions(userService);
    }

    @Test
    void resetPassword_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        ResetPasswordRequestDto request = validResetPasswordRequest();

        mockMvc.perform(
                        put("/users/1/reset-password")
                                .with(authority("user-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void resetPassword_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        ResetPasswordRequestDto request = validResetPasswordRequest();

        mockMvc.perform(
                        put("/users/1/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "password",
            "PASSWORD",
            "password123",
            "PASSWORD123"
    })
    void resetPassword_ShouldReturn400_WhenNewPasswordIsInvalid(
            String invalidPassword) throws Exception {

        ResetPasswordRequestDto request = validResetPasswordRequest();
        request.setNewPassword(invalidPassword);

        mockMvc.perform(
                        put("/users/1/reset-password")
                                .with(authority("user-reset-password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("newPassword: Invalid Password")));

        verifyNoInteractions(userService);
    }


    // ==================== Test Data ====================

    private UserCreateRequestDto validCreateRequest() {

        return UserCreateRequestDto.builder()
                .employee(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .username("test.user")
                .password("Password123")
                .usertype(
                        UserTypeDto.builder()
                                .id(1)
                                .build()
                )
                .remarks("Test user")
                .build();
    }

    private UserUpdateRequestDto validUpdateRequest() {

        return UserUpdateRequestDto.builder()
                .id(1)
                .employee(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .username("test.user")
                .usertype(
                        UserTypeDto.builder()
                                .id(1)
                                .build()
                )
                .remarks("Updated test user")
                .build();
    }

    private UserActiveDeactiveDto validActiveDeactiveRequest() {

        return UserActiveDeactiveDto.builder()
                .username("test.user")
                .accountLocked(false)
                .build();
    }

    private ChangePasswordRequestDto validChangePasswordRequest() {

        return ChangePasswordRequestDto.builder()
                .currentPassword("OldPassword123")
                .newPassword("NewPassword123")
                .build();
    }

    private ResetPasswordRequestDto validResetPasswordRequest() {

        return ResetPasswordRequestDto.builder()
                .newPassword("NewPassword123")
                .build();
    }

    private UserDetailResponseDto userResponse() {

        return UserDetailResponseDto.builder()
                .id(1)
                .employee(
                        EmployeeSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .username("test.user")
                .usertype(
                        UserTypeDto.builder()
                                .id(1)
                                .build()
                )
                .userstatus(
                        UserStatusDto.builder()
                                .id(1)
                                .build()
                )
                .accountlocked(false)
                .remarks("Test user")
                .build();
    }


    // ==================== Helpers ====================

    private RequestPostProcessor authority(String authority) {
        return user("testuser")
                .authorities(
                        new SimpleGrantedAuthority(authority)
                );
    }

    private String json(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
