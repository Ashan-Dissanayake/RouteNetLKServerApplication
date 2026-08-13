package lk.ashan.routenetlkserverapllication.module.vehicleservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.service.VehicleServiceIdentificationService;
import lk.ashan.routenetlkserverapllication.shared.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleServiceController.class)
@Import(TestSecurityConfiguration.class)
class VehicleServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleServiceIdentificationService vehicleServiceIdentificationService;

    @Autowired
    private ObjectMapper objectMapper;


    // ==================== GET /vehicle-services ====================

    @Test
    void getVehicleServices_ShouldReturn200() throws Exception {

        when(vehicleServiceIdentificationService.getVehicleServices())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/vehicle-services")
                                .with(authority("vehicle-service-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta.count").value(0));

        verify(vehicleServiceIdentificationService)
                .getVehicleServices();
    }

    @Test
    void getVehicleServices_ShouldReturn403_WhenUserDoesNotHaveVehicleServiceViewAuthority()
            throws Exception {

        mockMvc.perform(
                        get("/vehicle-services")
                                .with(authority("vehicle-view"))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void getVehicleServices_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        get("/vehicle-services")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }


    // ==================== POST /vehicle-services ====================

    @Test
    void createVehicleService_ShouldReturn201_WhenRequestIsValid()
            throws Exception {

        VehicleServiceCreateRequestDto request =
                validCreateRequest();

        when(vehicleServiceIdentificationService
                .createVehicleService(any(VehicleServiceCreateRequestDto.class)))
                .thenReturn(vehicleServiceResponse());

        mockMvc.perform(
                        post("/vehicle-services")
                                .with(authority("vehicle-service-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.meta.status").value("created"));

        verify(vehicleServiceIdentificationService)
                .createVehicleService(
                        any(VehicleServiceCreateRequestDto.class)
                );
    }

    @Test
    void createVehicleService_ShouldReturn403_WhenUserLacksVehicleServiceAddAuthority()
            throws Exception {

        VehicleServiceCreateRequestDto request =
                validCreateRequest();

        mockMvc.perform(
                        post("/vehicle-services")
                                .with(authority("vehicle-service-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void createVehicleService_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        VehicleServiceCreateRequestDto request =
                validCreateRequest();

        mockMvc.perform(
                        post("/vehicle-services")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }


    @ParameterizedTest
    @MethodSource("createRequestMissingFieldProvider")
    void createVehicleService_ShouldReturn400_WhenRequiredFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<VehicleServiceCreateRequestDto> mutator)
            throws Exception {

        VehicleServiceCreateRequestDto request =
                validCreateRequest();

        mutator.accept(request);

        mockMvc.perform(
                        post("/vehicle-services")
                                .with(authority("vehicle-service-add"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(field + ": " + errorMessage)));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    static Stream<Arguments> createRequestMissingFieldProvider() {

        return Stream.of(
                Arguments.of(
                        "branch",
                        "Branch is required",
                        (Consumer<VehicleServiceCreateRequestDto>)
                                dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "vehicle",
                        "Vehicle is required",
                        (Consumer<VehicleServiceCreateRequestDto>)
                                dto -> dto.setVehicle(null)
                ),

                Arguments.of(
                        "vehicleservicetype",
                        "Service Type is required",
                        (Consumer<VehicleServiceCreateRequestDto>)
                                dto -> dto.setVehicleservicetype(null)
                ),

                Arguments.of(
                        "vehicleservicestatus",
                        "Service Status is required",
                        (Consumer<VehicleServiceCreateRequestDto>)
                                dto -> dto.setVehicleservicestatus(null)
                ),

                Arguments.of(
                        "vehicleservicepriority",
                        "Service Priority is required",
                        (Consumer<VehicleServiceCreateRequestDto>)
                                dto -> dto.setVehicleservicepriority(null)
                ),

                Arguments.of(
                        "vehicleserviceparts",
                        "Service Parts are required",
                        (Consumer<VehicleServiceCreateRequestDto>)
                                dto -> dto.setVehicleserviceparts(null)
                )
        );
    }


    // ==================== POST /vehicle-services/{id}/start ====================

    @Test
    void startExecution_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        Integer id = 1;

        VehicleServiceStartRequestDto request =
                validStartRequest();

        when(vehicleServiceIdentificationService
                .startExecution(
                        eq(id),
                        any(VehicleServiceStartRequestDto.class)
                ))
                .thenReturn(vehicleServiceResponse());

        mockMvc.perform(
                        post("/vehicle-services/{id}/start", id)
                                .with(authority("vehicle-service-start"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists());

        verify(vehicleServiceIdentificationService)
                .startExecution(
                        eq(id),
                        any(VehicleServiceStartRequestDto.class)
                );
    }

    @Test
    void startExecution_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        Integer id = 1;

        VehicleServiceStartRequestDto request =
                validStartRequest();

        mockMvc.perform(
                        post("/vehicle-services/{id}/start", id)
                                .with(authority("vehicle-service-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void startExecution_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        Integer id = 1;

        VehicleServiceStartRequestDto request =
                validStartRequest();

        mockMvc.perform(
                        post("/vehicle-services/{id}/start", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }


    @Test
    void startExecution_ShouldReturn400_WhenStartOdometerIsMissing()
            throws Exception {

        VehicleServiceStartRequestDto request =
                validStartRequest();

        request.setStartodometer(null);

        mockMvc.perform(
                        post("/vehicle-services/{id}/start", 1)
                                .with(authority("vehicle-service-start"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "startodometer: Start odometer reading is required"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void startExecution_ShouldReturn400_WhenStartOdometerIsNegative()
            throws Exception {

        VehicleServiceStartRequestDto request =
                validStartRequest();

        request.setStartodometer(-1);

        mockMvc.perform(
                        post("/vehicle-services/{id}/start", 1)
                                .with(authority("vehicle-service-start"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "startodometer: Start odometer reading cannot be negative"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void startExecution_ShouldReturn400_WhenMainTechnicianIsMissing()
            throws Exception {

        VehicleServiceStartRequestDto request =
                validStartRequest();

        request.setMaintechnicianId(null);

        mockMvc.perform(
                        post("/vehicle-services/{id}/start", 1)
                                .with(authority("vehicle-service-start"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "maintechnicianId: Main technician ID is required"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }


    // ==================== POST /vehicle-services/{id}/hold-parts ====================

    @Test
    void placeOnHold_ShouldReturn200_WhenUserHasAuthority()
            throws Exception {

        Integer id = 1;

        when(vehicleServiceIdentificationService.placeOnHold(id))
                .thenReturn(vehicleServiceResponse());

        mockMvc.perform(
                        post("/vehicle-services/{id}/hold-parts", id)
                                .with(authority("vehicle-service-hold"))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists());

        verify(vehicleServiceIdentificationService)
                .placeOnHold(id);
    }

    @Test
    void placeOnHold_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        mockMvc.perform(
                        post("/vehicle-services/{id}/hold-parts", 1)
                                .with(authority("vehicle-service-view"))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void placeOnHold_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        mockMvc.perform(
                        post("/vehicle-services/{id}/hold-parts", 1)
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }


    // ==================== POST /vehicle-services/{id}/complete ====================

    @Test
    void complete_ShouldReturn200_WhenRequestIsValid()
            throws Exception {

        Integer id = 1;

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        when(vehicleServiceIdentificationService
                .complete(
                        eq(id),
                        any(VehicleServiceCompleteRequestDto.class)
                ))
                .thenReturn(vehicleServiceResponse());

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", id)
                                .with(authority("vehicle-service-complete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists());

        verify(vehicleServiceIdentificationService)
                .complete(
                        eq(id),
                        any(VehicleServiceCompleteRequestDto.class)
                );
    }

    @Test
    void complete_ShouldReturn403_WhenUserHasWrongAuthority()
            throws Exception {

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", 1)
                                .with(authority("vehicle-service-view"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isForbidden());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void complete_ShouldReturn401_WhenUserIsAnonymous()
            throws Exception {

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(vehicleServiceIdentificationService);
    }


    @Test
    void complete_ShouldReturn400_WhenRemarksIsMissing()
            throws Exception {

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        request.setRemarks(null);

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", 1)
                                .with(authority("vehicle-service-complete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "remarks: Completion remarks/technical report is required"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void complete_ShouldReturn400_WhenRemarksIsBlank()
            throws Exception {

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        request.setRemarks("");

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", 1)
                                .with(authority("vehicle-service-complete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "remarks: Completion remarks/technical report is required"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void complete_ShouldReturn400_WhenServiceIntervalKmIsMissing()
            throws Exception {

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        request.setServiceIntervalKm(null);

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", 1)
                                .with(authority("vehicle-service-complete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "serviceIntervalKm: Service interval distance (in KM) is required"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void complete_ShouldReturn400_WhenServiceIntervalKmIsZero()
            throws Exception {

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        request.setServiceIntervalKm(0);

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", 1)
                                .with(authority("vehicle-service-complete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "serviceIntervalKm: Service interval must be at least 1 KM"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }

    @Test
    void complete_ShouldReturn400_WhenServiceIntervalKmIsNegative()
            throws Exception {

        VehicleServiceCompleteRequestDto request =
                validCompleteRequest();

        request.setServiceIntervalKm(-1);

        mockMvc.perform(
                        post("/vehicle-services/{id}/complete", 1)
                                .with(authority("vehicle-service-complete"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details")
                        .value(hasItem(
                                "serviceIntervalKm: Service interval must be at least 1 KM"
                        )));

        verifyNoInteractions(vehicleServiceIdentificationService);
    }


    // ==================== Test Data ====================

    private VehicleServiceCreateRequestDto validCreateRequest() {

        return VehicleServiceCreateRequestDto.builder()
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .vehicle(
                        VehicleSummaryDto.builder()
                                .id(1)
                                .number("NB-1234")
                                .branchId(1)
                                .build()
                )
                .vehicleservicetype(
                        VehicleServiceTypeDto.builder()
                                .id(1)
                                .build()
                )
                .incident(
                        IncidentSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .vehicleservicestatus(
                        VehicleServiceStatusDto.builder()
                                .id(1)
                                .build()
                )
                .vehicleservicepriority(
                        VehicleServicePriorityDto.builder()
                                .id(1)
                                .build()
                )
                .vehicleserviceparts(List.of())
                .build();
    }

    private VehicleServiceStartRequestDto validStartRequest() {

        return VehicleServiceStartRequestDto.builder()
                .startodometer(10000)
                .maintechnicianId(1)
                .build();
    }

    private VehicleServiceCompleteRequestDto validCompleteRequest() {

        return VehicleServiceCompleteRequestDto.builder()
                .remarks("Service completed successfully")
                .serviceIntervalKm(5000)
                .build();
    }

    private VehicleServiceDetailResponseDto vehicleServiceResponse() {

        return VehicleServiceDetailResponseDto.builder()
                .id(1)
                .branch(
                        BranchSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .number("NB-1234")
                .vehicle(
                        VehicleSummaryDto.builder()
                                .id(1)
                                .number("NB-1234")
                                .branchId(1)
                                .build()
                )
                .vehicleservicetype(
                        VehicleServiceTypeDto.builder()
                                .id(1)
                                .build()
                )
                .incident(
                        IncidentSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .vehicleservicestatus(
                        VehicleServiceStatusDto.builder()
                                .id(1)
                                .build()
                )
                .vehicleservicepriority(
                        VehicleServicePriorityDto.builder()
                                .id(1)
                                .build()
                )
                .docreated(LocalDate.now())
                .vehicleserviceparts(List.of())
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

    private String json(Object object)
            throws JsonProcessingException {

        return objectMapper.writeValueAsString(object);
    }
}
