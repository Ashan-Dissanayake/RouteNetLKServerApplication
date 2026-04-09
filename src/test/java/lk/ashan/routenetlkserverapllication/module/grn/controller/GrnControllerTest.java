package lk.ashan.routenetlkserverapllication.module.grn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ashan.routenetlkserverapllication.BaseTest;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnPartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartRepository;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = "/modules/branch/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/branch/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/part/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/part/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/partrequest/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/partrequest/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/grn/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/modules/grn/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GrnControllerTest extends BaseTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PartRequestRepository partRequestRepository;
    @Autowired
    private PartRepository partRepository;

    @Autowired
    private GrnRepository grnRepository;

    private final String API_URL = "/grns";

    @Test
    void updateGrn_shouldCompletePO_whenFullQuantityReceived() throws Exception {
        GrnUpdateRequestDto dto = GrnUpdateRequestDto.builder()
                .id(1) // From your SQL: Draft GRN for Branch 1
                .doreceived(LocalDate.now())
                .grnpartrequestitems(List.of(
                        GrnPartRequestItemDto.builder()
                                .id(1) // The grnpartrequestitem ID
                                .quantity(new BigDecimal("10.000")) // Matches expected 10.000
                                .build()
                ))
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.grnstatus.name").value("Received"));

        assertEquals("Completed", partRequestRepository.findById(5).get().getPartrequeststatus().getName());
        assertEquals(new BigDecimal("15.000"), partRepository.findById(3).get().getQoh());

    }

    @Test
    void updateGrn_shouldCreateNewDraft_whenPartialQuantityReceived() throws Exception {
        GrnUpdateRequestDto dto = GrnUpdateRequestDto.builder()
                .id(3) // Linked to PR ID 6 (Expected 20)
                .doreceived(LocalDate.now())
                .grnpartrequestitems(List.of(
                        GrnPartRequestItemDto.builder().id(3).quantity(new BigDecimal("12.000")).build()
                ))
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.grnstatus.name").value("Partially Received"));

        // Verify PO remains Approved (Backorder)
        assertEquals("Approved", partRequestRepository.findById(6).get().getPartrequeststatus().getName());

        // Verify a NEW Balance Draft exists for the remaining 8.000 units
        List<Grn> drafts = grnRepository.findByGrnstatus_Name("Draft");
        boolean draftExists = drafts.stream()
                .anyMatch(g -> g.getGrnpartrequestitems().stream()
                        .anyMatch(i -> i.getQuantity().compareTo(new BigDecimal("8.000")) == 0));
        assertTrue(draftExists, "A balance draft for 8.000 units should have been created");
    }


    @Test
    void updateGrn_shouldHandleMixedItemsCorrectly() throws Exception {
        // PR ID 2: Item 1 (ordered 10)
        // Receiving 10 (Full) and another hypothetical line (Partial)
        GrnUpdateRequestDto dto = GrnUpdateRequestDto.builder()
                .id(1)
                .doreceived(LocalDate.now())
                .grnpartrequestitems(List.of(
                        GrnPartRequestItemDto.builder().id(1).quantity(new BigDecimal("5.000")).build()
                ))
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.grnstatus.name").value("Partially Received"));
    }

    @Test
    void updateGrn_shouldFail_whenQuantityExceedsBalance() throws Exception {
        GrnUpdateRequestDto dto = GrnUpdateRequestDto.builder()
                .id(2) // Expected 15
                .grnpartrequestitems(List.of(
                        GrnPartRequestItemDto.builder().id(2).quantity(new BigDecimal("20.000")).build()
                ))
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").value("Cannot receive more than the remaining balance."));
    }

    @Test
    void updateGrn_shouldNotUpdateStatus_whenPartIsDecommissioned() throws Exception {
        // Part 9 is Decommissioned. Initial QOH 10.
        GrnUpdateRequestDto dto = GrnUpdateRequestDto.builder()
                .id(5) // GRN linking to Part 9
                .grnpartrequestitems(List.of(
                        GrnPartRequestItemDto.builder().id(5).quantity(new BigDecimal("5.000")).build()
                ))
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Status must remain Decommissioned (State Pattern lock)
        assertEquals("Decommissioned", partRepository.findById(9).get().getPartstatus().getName());
        // QOH still increases (Physical arrival)
        assertEquals(0, new BigDecimal("15.000").compareTo(partRepository.findById(9).get().getQoh()));
    }

    @Test
    void updateGrn_shouldFail_whenQuantityIsZero() throws Exception {
        GrnUpdateRequestDto dto = GrnUpdateRequestDto.builder()
                .id(2)
                .grnpartrequestitems(List.of(
                        GrnPartRequestItemDto.builder().id(2).quantity(BigDecimal.ZERO).build()
                ))
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
