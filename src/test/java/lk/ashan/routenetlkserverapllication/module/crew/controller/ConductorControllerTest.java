package lk.ashan.routenetlkserverapllication.module.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.config.ValidationResultMatcher;
import lk.ashan.routenetlkserverapllication.config.factory.ConductorDtoFactory;
import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorCreateRequestDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ConductorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String apiUrl = "/conductors";

    @ParameterizedTest
    @MethodSource("missingFieldProvider")
    void createConductor_shouldFail_whenFieldIsMissing(String field, String errorMessage, Consumer<ConductorCreateRequestDto> mutator) throws Exception {
        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
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
                Arguments.of("number", "Number can not be empty", (Consumer<ConductorCreateRequestDto>) dto -> dto.setNumber(null)),
                Arguments.of("domedicalissued", "Medical issued date is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setDomedicalissued(null)),
                Arguments.of("domedicalexpired", "Medical expired date is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setDomedicalexpired(null)),
                Arguments.of("crewstatus", "Crew status is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setCrewstatus(null)),
                Arguments.of("routefamiliaritylevel", "Route Familiarity Level is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setRoutefamiliaritylevel(null)),
                Arguments.of("employee", "Employee is mandatory", (Consumer<ConductorCreateRequestDto>) dto -> dto.setEmployee(null))
        );
    }

    // Conductor Number - Invalid patterns
    @ParameterizedTest
    @ValueSource(strings = {
            "CON1234-123",    // missing hyphen after CON
            "CON-123-123",    // only 3 digits instead of 4
            "CON-12345-123",  // 5 digits instead of 4
            "CON-1234-12",    // only 2 digits instead of 3
            "con-1234-123",   // lowercase prefix
            "CON-12A4-123",   // letter in first number block
            "CON-1234-12B",   // letter in second number block
            " CON-1234-123",  // leading space
            "CON-1234-123 ",  // trailing space
            "CON--1234-123",  // double hyphen
            "CON-1234_123",   // underscore instead of hyphen
            "ABC-1234-123"    // wrong prefix
    })
    void createConductor_shouldFail_whenNumberFormatIsInvalid(String invalidNumber) throws Exception {
        ConductorCreateRequestDto dto = ConductorDtoFactory.createUniqueConductorCreateRequest();
        dto.setNumber(invalidNumber);

        mockMvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(ValidationResultMatcher.expectValidationError(
                        "number: Invalid conductor number"
                ));
    }

    
}
