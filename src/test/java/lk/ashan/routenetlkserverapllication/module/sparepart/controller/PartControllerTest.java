package lk.ashan.routenetlkserverapllication.module.sparepart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/part/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/part/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PartControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/parts";

    @ParameterizedTest
    @MethodSource("missingPartFieldProvider")
    void createPart_shouldFail_whenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PartCreateRequestDto> mutator
    ) throws Exception {

        PartCreateRequestDto dto = PartCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
                .partmaster(PartMasterDto.builder().id(2).sku("ENG-002").name("Fan Belt").build())
                .qoh(new BigDecimal("60"))
                .maxlevel(new BigDecimal("120"))
                .rop(new BigDecimal("40"))
                .dolastordered(LocalDate.of(2026, 2, 15))
                .partstatus(PartStatusDto.builder().id(1).name("Available").build())
                .build();

        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingPartFieldProvider() {
        return Stream.of(

                Arguments.of(
                        "branch",
                        "Branch is required",
                        (Consumer<PartCreateRequestDto>) dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "partmaster",
                        "Part Master is required",
                        (Consumer<PartCreateRequestDto>) dto -> dto.setPartmaster(null)
                ),

                Arguments.of(
                        "qoh",
                        "QOH is required",
                        (Consumer<PartCreateRequestDto>) dto -> dto.setQoh(null)
                ),

                Arguments.of(
                        "maxlevel",
                        "Max level is required",
                        (Consumer<PartCreateRequestDto>) dto -> dto.setMaxlevel(null)
                ),

                Arguments.of(
                        "rop",
                        "ROP is required",
                        (Consumer<PartCreateRequestDto>) dto -> dto.setRop(null)
                ),

                Arguments.of(
                        "dolastordered",
                        "DO last ordered is required",
                        (Consumer<PartCreateRequestDto>) dto -> dto.setDolastordered(null)
                ),

                Arguments.of(
                        "partstatus",
                        "Part status is required",
                        (Consumer<PartCreateRequestDto>) dto -> dto.setPartstatus(null)
                )
        );
    }


    @ParameterizedTest
    @ValueSource(strings = { "-1", "-100" })
    void createPart_shouldFail_WhenQohIsNegative(String qohStr) throws Exception {

        PartCreateRequestDto dto = PartCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
                .partmaster(PartMasterDto.builder().id(2).sku("ENG-002").name("Fan Belt").build())
                .qoh(new BigDecimal(qohStr))
                .maxlevel(new BigDecimal("120"))
                .rop(new BigDecimal("40"))
                .dolastordered(LocalDate.of(2026, 2, 15))
                .partstatus(PartStatusDto.builder().id(1).name("Available").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem("qoh: QOH cannot be negative")));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "-1" })
    void createPart_shouldFail_WhenMaxLevelIsLessThan1(String value) throws Exception {

        PartCreateRequestDto dto = PartCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
                .partmaster(PartMasterDto.builder().id(2).sku("ENG-002").name("Fan Belt").build())
                .qoh(new BigDecimal("60"))
                .maxlevel(new BigDecimal(value))
                .rop(new BigDecimal("40"))
                .dolastordered(LocalDate.of(2026, 2, 15))
                .partstatus(PartStatusDto.builder().id(1).name("Available").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem("maxlevel: Max level must be greater than 0")));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "-1" })
    void createPart_shouldFail_WhenRopIsLessThan1(String value) throws Exception {

        PartCreateRequestDto dto = PartCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
                .partmaster(PartMasterDto.builder().id(2).sku("ENG-002").name("Fan Belt").build())
                .qoh(new BigDecimal("60"))
                .maxlevel(new BigDecimal("120"))
                .rop(new BigDecimal(value))
                .dolastordered(LocalDate.of(2026, 2, 15))
                .partstatus(PartStatusDto.builder().id(1).name("Available").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem("rop: ROP must be greater than 0")));
    }

    @ParameterizedTest
    @ValueSource(strings = { "2030-01-01", "2999-12-31" })
    void createPart_shouldFail_WhenDoLastOrderedIsInFuture(String futureDate) throws Exception {

        PartCreateRequestDto dto = PartCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
                .partmaster(PartMasterDto.builder().id(2).sku("ENG-002").name("Fan Belt").build())
                .qoh(new BigDecimal("60"))
                .maxlevel(new BigDecimal("120"))
                .rop(new BigDecimal("40"))
                .dolastordered(LocalDate.parse(futureDate))
                .partstatus(PartStatusDto.builder().id(1).name("Available").build())
                .build();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",hasItem("dolastordered: Date cannot be in the future")));
    }

//    @Test
//    void createPart_shouldFail_whenPartIsExist() throws Exception{
//        PartCreateRequestDto dto = PartCreateRequestDto.builder()
//                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
//                .partmaster(PartMasterDto.builder().id(2).sku("ENG-002").name("Fan Belt").build())
//                .qoh(new BigDecimal("60"))
//                .maxlevel(new BigDecimal("120"))
//                .rop(new BigDecimal("40"))
//                .dolastordered(LocalDate.now().minusDays(20))
//                .partstatus(PartStatusDto.builder().id(1).name("Available").build())
//                .build();
//
//        mockMvc.perform(post(API_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isConflict())
//                .andExpect(jsonPath("$.details",hasItem("This part already exists")));
//    }

    @ParameterizedTest
    @MethodSource("missingUpdateFieldProvider")
    void updatePart_shouldFail_whenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PartUpdateRequestDto> mutator
    ) throws Exception {

        PartUpdateRequestDto dto = validUpdateDto();

        mutator.accept(dto);

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details",
                        hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingUpdateFieldProvider() {
        return Stream.of(
                Arguments.of(
                        "branch",
                        "Branch is required",
                        (Consumer<PartUpdateRequestDto>) dto -> dto.setBranch(null)
                ),

                Arguments.of(
                        "partmaster",
                        "Part Master is required",
                        (Consumer<PartUpdateRequestDto>) dto -> dto.setPartmaster(null)
                ),

                Arguments.of(
                        "qoh",
                        "QOH is required",
                        (Consumer<PartUpdateRequestDto>) dto -> dto.setQoh(null)
                ),

                Arguments.of(
                        "maxlevel",
                        "Max level is required",
                        (Consumer<PartUpdateRequestDto>) dto -> dto.setMaxlevel(null)
                ),

                Arguments.of(
                        "rop",
                        "ROP is required",
                        (Consumer<PartUpdateRequestDto>) dto -> dto.setRop(null)
                ),

                Arguments.of(
                        "dolastordered",
                        "DO last ordered is required",
                        (Consumer<PartUpdateRequestDto>) dto -> dto.setDolastordered(null)
                ),

                Arguments.of(
                        "partstatus",
                        "Part status is required",
                        (Consumer<PartUpdateRequestDto>) dto -> dto.setPartstatus(null)
                )
        );
    }

    @Test
    void updatePart_shouldSucceed_whenValidRequest() throws Exception {

        PartUpdateRequestDto dto = validUpdateDto();

        dto.setMaxlevel(new BigDecimal("150"));

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void updatePart_shouldFail_whenPartNotFound() throws Exception {

        PartUpdateRequestDto dto = validUpdateDto();

        dto.setId(9999);

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.details")
                        .value("Part not found"));
    }

    @Test
    void updatePart_shouldFail_whenMaxLevelLessThanOrEqualToRop() throws Exception {

        PartUpdateRequestDto dto = validUpdateDto();

        dto.setMaxlevel(new BigDecimal("39"));
        dto.setRop(new BigDecimal("40"));

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details")
                        .value("Max level must be greater than reorder point"));
    }

    @Test
    void updatePart_shouldFail_whenMaxLevelLessThanExistingQoh() throws Exception {

        PartUpdateRequestDto dto = validUpdateDto();

        dto.setId(1);

        dto.setMaxlevel(new BigDecimal("10"));

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details")
                        .value("Max level must be greater than reorder point"));
    }

    @Test
    void updatePart_shouldFail_whenInvalidStatusTransition() throws Exception {

        PartUpdateRequestDto dto = validUpdateDto();

        dto.setId(9);

        dto.setPartstatus(
                PartStatusDto.builder()
                        .id(1)
                        .name("AVAILABLE")
                        .build()
        );

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details")
                        .value("No transitions allowed from DECOMMISSIONED state"));
    }

    private PartUpdateRequestDto validUpdateDto() {

        return PartUpdateRequestDto.builder()
                .id(1)
                .branch(
                        BranchSummaryDto.builder()
                                .id(2)
                                .name("Angoda")
                                .build()
                )
                .partmaster(
                        PartMasterDto.builder()
                                .id(2)
                                .sku("ENG-002")
                                .name("Fan Belt")
                                .build()
                )
                .qoh(new BigDecimal("60"))
                .maxlevel(new BigDecimal("120"))
                .rop(new BigDecimal("40"))
                .dolastordered(LocalDate.now().minusDays(10))
                .partstatus(
                        PartStatusDto.builder()
                                .id(1)
                                .name("AVAILABLE")
                                .build()
                )
                .build();
    }

}

