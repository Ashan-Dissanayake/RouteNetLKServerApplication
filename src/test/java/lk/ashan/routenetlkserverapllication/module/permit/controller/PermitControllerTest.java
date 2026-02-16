package lk.ashan.routenetlkserverapllication.module.permit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.config.factory.ConductorDtoFactory;
import lk.ashan.routenetlkserverapllication.config.factory.PermitDtoFactory;
import lk.ashan.routenetlkserverapllication.config.factory.VehicleDtoFactory;
import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitCreateRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = "spring.sql.init.mode=never")
class PermitControllerTest {

    //@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/permits";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createPermit_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<PermitCreateRequestDto> mutator) throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        mutator.accept(dto);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        field + ": " + errorMessage
                ));
    }

    static Stream<Arguments> missingFieldProvider() {
        return Stream.of(
                Arguments.of("number", "Number is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setNumber(null)),
                Arguments.of("vehicle", "Vehicle is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setVehicle(null)),
                Arguments.of("doissued", "Date of issued is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setDoissued(null)),
                Arguments.of("doexpired", "Date of Exp is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setDoexpired(null)),
                Arguments.of("branch", "Branch is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setBranch(null)),
                Arguments.of("permitestatus", "Permit status is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setPermitestatus(null)),
                Arguments.of("servicetype", "Service type is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setServicetype(null)),
                Arguments.of("route", "Route is mandatory", (Consumer<PermitCreateRequestDto>) dto -> dto.setRoute(null))
        );
    }

    // Permit Number - Invalid patterns
    @ParameterizedTest
    @ValueSource(strings = {
            "269",                      // only 3 digits
            "26967",                    // 5 digits instead of 4
            "ANG-NA-7845-103",          // extra hyphen after NA
            "ANG-na7845-103",           // lowercase letters
            "ANG-NA78A5-103",           // letter in digit block
            "ANG-NA7845",               // missing numeric suffix
            "ANG-NA7845-",              // trailing hyphen
            "ANG-NA7845-103-",          // trailing hyphen after numbers
            "ANG--NA7845-103",          // double hyphen
            "ANG_NA7845-103",           // underscore instead of hyphen
            "AN-NA7845-103",            // only 2 letters in first block
            "ANG-N7845-103",            // only 1 letter before digits
            " ang-NA7845-103",          // leading space
            "ANG-NA7845-103 ",          // trailing space
            "ANG-NA7845-103\\",         // dangling backslash
            "ANG-NA7845-103\\ang-NA1"   // lowercase in chained permit
    })
    void createPermit_shouldFail_whenPermitNumberFormatIsInvalid(String invalidPermit) throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        dto.setNumber(invalidPermit);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid Permit Number"
                ));
    }

    // Permit Number - Already Exists
    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createPermit_shouldFail_whenPermitNumberAlreadyExists() throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        dto.setNumber("ANG-NA7845-103-3");

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Permit number already exists."
                ));
    }

    //Vehicle Not found
    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createPermit_shouldFail_whenVehicleNotExists() throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        dto.setVehicle(VehicleDtoFactory.vehicleSummaryResponseDto(16,"NG-1299"));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Vehicle not found"
                ));
    }

    //Route Not found
    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createPermit_shouldFail_whenRouteNotExists() throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        dto.setRoute(PermitDtoFactory.routeSummaryRequestDto(19,"15",30));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Route not found"
                ));
    }

    //Service Not found
    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createPermit_shouldFail_whenServiceTypeNotExists() throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        dto.setServicetype(PermitDtoFactory.serviceTypeDto(4,"Highway"));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Service type not found"
                ));
    }

    //Invalid Status
    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createPermit_shouldFail_whenPermitStatusIsInvalid() throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        dto.setPermitestatus(PermitDtoFactory.permitStatusDto(2,"Expired"));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "This state is not allowed as initial state"
                ));
    }

    //Bus Type Route Type Invalid Combination
    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createPermit_shouldFail_whenBusTypeAndRouteTypeIsInvalidCombination() throws Exception {
        PermitCreateRequestDto dto = PermitDtoFactory.createUniquePermitRequestDto();
        dto.setVehicle(VehicleDtoFactory.vehicleSummaryResponseDto(2,"ND-9167"));

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "Invalid combination: Type C buses cannot be used on inter provincial route."
                ));
    }
}
