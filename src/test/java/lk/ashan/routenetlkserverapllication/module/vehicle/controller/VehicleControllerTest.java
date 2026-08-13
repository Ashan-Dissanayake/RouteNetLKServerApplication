package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehicleService;
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

@WebMvcTest(VehicleController.class)
@Import(TestSecurityConfiguration.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;


    // ==================== GET /vehicles ====================

    @Test
    void getVehicles_ShouldReturn200() throws Exception {

        when(vehicleService.getVehicles())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/vehicles")
                                .with(authority("vehicle-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(vehicleService).getVehicles();
    }

    @Test
    void getVehicles_ShouldReturn403_WhenUserDoesNotHaveVehicleViewAuthority()
            throws Exception {

        mockMvc.perform(
                        get("/vehicles")
                                .with(authority("branch-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleService);
    }

    @Test
    void getVehicles_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        get("/vehicles")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleService);
    }


    // ==================== GET /vehicles/summaries ====================

    @Test
    void getSummaries_ShouldReturn200_WhenAuthenticated() throws Exception {

        when(vehicleService.getVehicleSummary())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/vehicles/summaries")
                                .with(authenticatedUser())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(vehicleService).getVehicleSummary();
    }


    // ==================== POST /vehicles ====================

    @Test
    void createVehicle_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        VehicleCreateRequestDto request = validCreateRequest();

        when(vehicleService.createVehicle(any(VehicleCreateRequestDto.class)))
                .thenReturn(vehicleResponse());

        mockMvc.perform(
                        post("/vehicles")
                                .with(authority("vehicle-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists());

        verify(vehicleService)
                .createVehicle(any(VehicleCreateRequestDto.class));
    }

    @Test
    void createVehicle_ShouldReturn403_WhenUserLacksVehicleAddAuthority()
            throws Exception {

        VehicleCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post("/vehicles")
                                .with(authority("vehicle-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleService);
    }

    @Test
    void createVehicle_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        VehicleCreateRequestDto request = validCreateRequest();

        mockMvc.perform(
                        post("/vehicles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleService);
    }

    @ParameterizedTest
    @MethodSource("createVehicleMissingFieldProvider")
    void createVehicle_ShouldReturn400_WhenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<VehicleCreateRequestDto> mutator) throws Exception {

        VehicleCreateRequestDto request = validCreateRequest();

        mutator.accept(request);

        mockMvc.perform(
                        post("/vehicles")
                                .with(authority("vehicle-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(vehicleService);
    }

    static Stream<Arguments> createVehicleMissingFieldProvider() {

        return Stream.of(
                Arguments.of(
                        "number",
                        "Plate Number Can Not be Empty",
                        (Consumer<VehicleCreateRequestDto>)
                                dto -> dto.setNumber(null)
                ),

                Arguments.of(
                        "fueltype",
                        "Fuel Type Can Not be Empty",
                        (Consumer<VehicleCreateRequestDto>)
                                dto -> dto.setFueltype(null)
                ),

                Arguments.of(
                        "conditionrate",
                        "Condition Rate Can Not be Empty",
                        (Consumer<VehicleCreateRequestDto>)
                                dto -> dto.setConditionrate(null)
                ),

                Arguments.of(
                        "vehiclestatus",
                        "Vehicle Status Can Not be Empty",
                        (Consumer<VehicleCreateRequestDto>)
                                dto -> dto.setVehiclestatus(null)
                ),

                Arguments.of(
                        "model",
                        "Model can not be Empty",
                        (Consumer<VehicleCreateRequestDto>)
                                dto -> dto.setModel(null)
                ),

                Arguments.of(
                        "bustype",
                        "Bus type can not be Empty",
                        (Consumer<VehicleCreateRequestDto>)
                                dto -> dto.setBustype(null)
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "NA-123",
            "NAB-123",
            "NA-12345",
            "NA1234",
            "A-1234",
            "NA-123A",
            "na-1234",
            "NAB-1234"
    })
    void createVehicle_ShouldReturn400_WhenPlateNumberIsInvalid(
            String invalidNumber) throws Exception {

        VehicleCreateRequestDto request = validCreateRequest();
        request.setNumber(invalidNumber);

        mockMvc.perform(
                        post("/vehicles")
                                .with(authority("vehicle-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("number: Invalid Plate Number")));

        verifyNoInteractions(vehicleService);
    }

    @Test
    void createVehicle_ShouldReturn400_WhenMileageIsNotPositive()
            throws Exception {

        VehicleCreateRequestDto request = validCreateRequest();
        request.setMileage(0);

        mockMvc.perform(
                        post("/vehicles")
                                .with(authority("vehicle-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("mileage: Mileage must be positive")));

        verifyNoInteractions(vehicleService);
    }


    // ==================== PUT /vehicles ====================

    @Test
    void updateVehicle_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        VehicleUpdateRequestDto request = validUpdateRequest();

        when(vehicleService.updateVehicle(any(VehicleUpdateRequestDto.class)))
                .thenReturn(vehicleResponse());

        mockMvc.perform(
                        put("/vehicles")
                                .with(authority("vehicle-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists());

        verify(vehicleService)
                .updateVehicle(any(VehicleUpdateRequestDto.class));
    }

    @Test
    void updateVehicle_ShouldReturn400_WhenRequestIsInvalid()
            throws Exception {

        VehicleUpdateRequestDto request = validUpdateRequest();
        request.setNumber(null);

        mockMvc.perform(
                        put("/vehicles")
                                .with(authority("vehicle-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "number: Plate Number Can Not be Empty"
                        )));

        verifyNoInteractions(vehicleService);
    }

    @Test
    void updateVehicle_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        VehicleUpdateRequestDto request = validUpdateRequest();

        mockMvc.perform(
                        put("/vehicles")
                                .with(authority("vehicle-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleService);
    }

    @Test
    void updateVehicle_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        VehicleUpdateRequestDto request = validUpdateRequest();

        mockMvc.perform(
                        put("/vehicles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleService);
    }

    @Test
    void updateVehicle_ShouldReturn400_WhenIdIsMissing()
            throws Exception {

        VehicleUpdateRequestDto request = validUpdateRequest();
        request.setId(null);

        mockMvc.perform(
                        put("/vehicles")
                                .with(authority("vehicle-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("id: Id Can Not be Empty")));

        verifyNoInteractions(vehicleService);
    }

    @Test
    void updateVehicle_ShouldReturn400_WhenMileageIsNotPositive()
            throws Exception {

        VehicleUpdateRequestDto request = validUpdateRequest();
        request.setMileage(0);

        mockMvc.perform(
                        put("/vehicles")
                                .with(authority("vehicle-update"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem("mileage: Mileage must be positive")));

        verifyNoInteractions(vehicleService);
    }


    // ==================== DELETE /vehicles ====================

    @Test
    void deactivateVehicles_ShouldReturn204_WhenRequestIsValid()
            throws Exception {

        List<Integer> ids = List.of(1, 2, 3);

        when(vehicleService.deactivateVehicle(anyList()))
                .thenReturn(ids);

        mockMvc.perform(
                        delete("/vehicles")
                                .with(authority("vehicle-delete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(ids))
                )
                .andExpect(status().isNoContent());

        verify(vehicleService).deactivateVehicle(anyList());
    }

    @Test
    void deactivateVehicles_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        List<Integer> ids = List.of(1, 2, 3);

        mockMvc.perform(
                        delete("/vehicles")
                                .with(authority("vehicle-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(ids))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleService);
    }

    @Test
    void deactivateVehicles_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        List<Integer> ids = List.of(1, 2, 3);

        mockMvc.perform(
                        delete("/vehicles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(ids))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleService);
    }


    // ==================== Test Data ====================

    private VehicleCreateRequestDto validCreateRequest() {

        return VehicleCreateRequestDto.builder()
                .number("NA-1234")
                .mileage(50000)
                .remarks("Test vehicle")
                .fueltype(
                        FueltypeDto.builder()
                                .id(1)
                                .build()
                )
                .conditionrate(
                        ConditionrateDto.builder()
                                .id(1)
                                .build()
                )
                .vehiclestatus(
                        VehiclestatusDto.builder()
                                .id(1)
                                .build()
                )
                .model(
                        ModelDto.builder()
                                .id(1)
                                .build()
                )
                .bustype(
                        BusTypeDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }

    private VehicleUpdateRequestDto validUpdateRequest() {

        return VehicleUpdateRequestDto.builder()
                .id(1)
                .number("NA-1234")
                .mileage(55000)
                .remarks("Updated test vehicle")
                .fueltype(
                        FueltypeDto.builder()
                                .id(1)
                                .build()
                )
                .conditionrate(
                        ConditionrateDto.builder()
                                .id(1)
                                .build()
                )
                .vehiclestatus(
                        VehiclestatusDto.builder()
                                .id(1)
                                .build()
                )
                .model(
                        ModelDto.builder()
                                .id(1)
                                .build()
                )
                .bustype(
                        BusTypeDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }

    private VehicleDetailResponseDto vehicleResponse() {

        return VehicleDetailResponseDto.builder()
                .id(1)
                .number("NA-1234")
                .model(
                        ModelDto.builder()
                                .id(1)
                                .build()
                )
                .bustype(
                        BusTypeDto.builder()
                                .id(1)
                                .build()
                )
                .mileage(50000)
                .fueltype(
                        FueltypeDto.builder()
                                .id(1)
                                .build()
                )
                .conditionrate(
                        ConditionrateDto.builder()
                                .id(1)
                                .build()
                )
                .remarks("Test vehicle")
                .vehiclestatus(
                        VehiclestatusDto.builder()
                                .id(1)
                                .build()
                )
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .build();
    }


    // ==================== Helpers ====================
    private RequestPostProcessor authenticatedUser() {
        return user("testuser");
    }

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
