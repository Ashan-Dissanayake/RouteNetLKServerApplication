package lk.ashan.routenetlkserverapllication.module.partreqest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartMasterDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/part/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/part/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/partrequest/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/partrequest/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/numberservice/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/numberservice/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PartRequestControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_URL = "/part-requests";

    //=========================
    // Mandatory fields test
    //=========================
    @ParameterizedTest
    @MethodSource("missingPartFieldProvider")
    void createPartRequest_shouldFail_whenFieldIsMissing(
            String field,
            String errorMessage,
            Consumer<PartRequestCreateRequestDto> mutator
    ) throws Exception {

        PartRequestCreateRequestDto dto = validPartRequestDto();
        mutator.accept(dto);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasItem(field + ": " + errorMessage)));
    }

    static Stream<Arguments> missingPartFieldProvider() {
        return Stream.of(
                Arguments.of("branch", "Branch is mandatory", (Consumer<PartRequestCreateRequestDto>) dto -> dto.setBranch(null)),
                Arguments.of("dorequested", "Requested date is mandatory", (Consumer<PartRequestCreateRequestDto>) dto -> dto.setDorequested(null)),
                Arguments.of("partrequeststatus", "Status is mandatory", (Consumer<PartRequestCreateRequestDto>) dto -> dto.setPartrequeststatus(null)),
                Arguments.of("partrequestitems", "Request items are mandatory", (Consumer<PartRequestCreateRequestDto>) dto -> dto.setPartrequestitems(null))
        );
    }

    //=========================
    // PastOrPresent validation
    //=========================
    @ParameterizedTest
    @ValueSource(strings = { "2030-01-01", "2999-12-31" })
    void createPartRequest_shouldFail_WhenDoRequestedIsFuture(String futureDate) throws Exception {
        PartRequestCreateRequestDto dto = validPartRequestDto();
        dto.setDorequested(LocalDate.parse(futureDate));

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasItem("dorequested: Requested date cannot be in the future")));
    }

    //=========================
    // Quantity validation
    //=========================
    @ParameterizedTest
    @ValueSource(strings = { "0", "-1" })
    void createPartRequest_shouldFail_WhenQuantityIsInvalid(String qty) throws Exception {
        PartRequestCreateRequestDto dto = validPartRequestDto();
        dto.getPartrequestitems().forEach(item -> item.setQuantity(new BigDecimal(qty)));

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",
                        hasItem("Requested quantity must be greater than zero for part: " + dto.getPartrequestitems().get(0).getPart().getName())));
    }

    //=========================
    // Duplicate part in same request
    //=========================
    @Test
    void createPartRequest_shouldFail_WhenDuplicatePartsInSameRequest() throws Exception {
        PartRequestCreateRequestDto dto = validPartRequestDto();
        // Add a duplicate part
        dto.getPartrequestitems().add(dto.getPartrequestitems().get(0));

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",
                        hasItem("Duplicate part in the same request: " + dto.getPartrequestitems().get(0).getPart().getName())));
    }

    //=========================
    // Duplicate part in open requests
    //=========================
    @Test
    void createPartRequest_shouldFail_WhenPartAlreadyRequestedOpen() throws Exception {

        PartRequestCreateRequestDto dto = validPartRequestDto();

        dto.setBranch(
                BranchSummaryDto.builder()
                        .id(1)
                        .name("Colombo")
                        .build()
        );

        dto.getPartrequestitems().get(0)
                .setPart(
                        PartSummaryDto.builder()
                                .id(1)
                                .name("Fan Belt")
                                .build()
                );

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details",
                        hasItem("Part Fan Belt has already been requested and is still pending.")));
    }

    //=========================
    // Success case
    //=========================
    @Test
    void createPartRequest_shouldSucceed_WhenValidRequest() throws Exception {
        PartRequestCreateRequestDto dto = validPartRequestDto();

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.number").exists())
                .andExpect(jsonPath("$.data.partrequestitems").isArray());
    }

    //=========================
    // Helper method
    //=========================
    private PartRequestCreateRequestDto validPartRequestDto() {
        return PartRequestCreateRequestDto.builder()
                .branch(BranchSummaryDto.builder().id(2).name("Angoda").build())
                .dorequested(LocalDate.now())
                .partrequeststatus(PartRequestStatusDto.builder().id(1).name("Pending").build())
                .partrequestitems(new ArrayList<>(List.of(
                        PartRequestItemDto.builder()
                                .part(PartSummaryDto.builder().id(2).name("Fan Belt").build())
                                .quantity(new BigDecimal("10"))
                                .build()
                )))
                .build();
    }
}
