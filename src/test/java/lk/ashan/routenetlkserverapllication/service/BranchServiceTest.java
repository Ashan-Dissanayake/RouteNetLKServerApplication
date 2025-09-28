package lk.ashan.routenetlkserverapllication.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchCoverageMapper;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.DistrictMapper;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchtype;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.util.factory.BranchTestDataFactory;
import lk.ashan.routenetlkserverapllication.util.factory.DtoFactory;
import lk.ashan.routenetlkserverapllication.util.factory.EntityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    private LocalDate fixedDate;
    private List<Branch> mockBranches;

    @Mock
    private BranchRepository branchRepository;
    @Spy
    private BranchCoverageMapper branchCoverageMapper = Mappers.getMapper(BranchCoverageMapper.class);
    @Spy
    private BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);
    @InjectMocks
    private BranchService branchService;



    @BeforeEach
    void setUp() {
        fixedDate = BranchTestDataFactory.getFixedDate();
        mockBranches = EntityFactory.buildMockBranches(fixedDate);
        ReflectionTestUtils.setField(branchMapper, "branchCoverageMapper", branchCoverageMapper);

    }


    // ────────────── RETRIEVE TESTS ──────────────

    @Test
    void getBranches_shouldReturnAllBranches() {
        when(branchRepository.findAll()).thenReturn(mockBranches);
        List<BranchDetailResponseDto> result = branchService.getBranches();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void searchBranch_shouldFilterByName() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("ssname", "Colombo Branch");

        List<BranchDetailResponseDto> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Colombo Branch", "CLB0001-1", 1);
    }

    @Test
    void searchBranch_shouldFilterByCode() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("sscode", "RAT0007-1");

        List<BranchDetailResponseDto> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Ratnapura Branch", "RAT0007-1", 2);
    }

    @Test
    void searchBranch_shouldFilterByStatusId() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("ssbranchstatus", "1");

        List<BranchDetailResponseDto> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
    }

    @Test
    void searchBranch_shouldApplyMultipleFiltersTogether() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("ssname", "Colombo Branch");
        params.put("ssbranchstatus", "1");

        List<BranchDetailResponseDto> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Colombo Branch", "CLB0001-1", 1);
    }

    @Test
    void searchBranch_shouldReturnMatchingWhenAllFiltersMatch() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> emptyParams = new HashMap<>();
        List<BranchDetailResponseDto> result = branchService.searchBranch(emptyParams);

        assertEquals(2, result.size());
    }


    // ────────────── CREATE TESTS ──────────────

    @Test
    void createBranch_shouldThrow_whenNameExists() {
        BranchCreateRequestDto branch = DtoFactory.createBranchRequest("Colombo Branch", "UNIQUE001-1","0665714150");

        when(branchRepository.existsByName(branch.getName())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenCodeExists() {
        BranchCreateRequestDto branch = DtoFactory.createBranchRequest("Unique Branch", "CLB0001-1","0665714150");
        when(branchRepository.existsByCode(branch.getCode())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenEmailExists() {
        BranchCreateRequestDto branch = DtoFactory.createBranchRequest("Unique Branch", "UNIQUE001-1","0665714150");
        when(branchRepository.existsByEmail(branch.getEmail())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenTelephoneExists() {
        BranchCreateRequestDto branch = DtoFactory.createBranchRequest("Unique Branch", "UNIQUE001-1", "0112345678");
        when(branchRepository.existsByTelephone(branch.getTelephone())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldSucceed_whenAllUnique() {
        // Arrange
        BranchCreateRequestDto branchCreateRequest =
                DtoFactory.createBranchRequest("Unique", "UNIQUE001-1", "0112223344");

        Branch branchEntity = branchMapper.toEntity(branchCreateRequest);

        when(branchRepository.existsByName(branchCreateRequest.getName())).thenReturn(false);
        when(branchRepository.existsByCode(branchCreateRequest.getCode())).thenReturn(false);
        when(branchRepository.existsByEmail(branchCreateRequest.getEmail())).thenReturn(false);
        when(branchRepository.existsByTelephone(branchCreateRequest.getTelephone())).thenReturn(false);
        when(branchRepository.save(any(Branch.class))).thenReturn(branchEntity);

        // Act
        BranchDetailResponseDto created = branchService.createBranch(branchCreateRequest);

        // Assert
        assertNotNull(created);
        assertEquals("Unique", created.getName());
    }

    // ────────────── UPDATE TESTS ──────────────

//    @Test
//    void updateBranch_shouldThrow_whenNameExists() {
//        BranchUpdateRequestDto branch = BranchTestDataFactory.buildUpdateBranchRequest("Colombo Branch","UNIQUE001-1","unique@ntc.gov.lk","0112223344");
//        branch.setId(2);
//
//        when(branchRepository.existsByNameAndIdNot(branch.getName(), branch.getId())).thenReturn(true);
//
//        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
//        verify(branchRepository, never()).save(any());
//    }
//
//    @Test
//    void updateBranch_shouldThrow_whenCodeExists() {
//        BranchUpdateRequestDto branch = BranchTestDataFactory.buildUpdateBranchRequest("Unique Name","CLB0001-1","unique@ntc.gov.lk","0112223344");
//        branch.setId(2);
//
//        when(branchRepository.existsByCodeAndIdNot(branch.getCode(), branch.getId())).thenReturn(true);
//
//        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
//        verify(branchRepository, never()).save(any());
//    }
//
//    @Test
//    void updateBranch_shouldThrow_whenEmailExists() {
//        BranchUpdateRequestDto branch = BranchTestDataFactory.buildUpdateBranchRequest("Unique Name","UNIQUE001-1","colombo@ntc.gov.lk","0112223344");
//        branch.setId(2);
//
//        when(branchRepository.existsByEmailAndIdNot(branch.getEmail(), branch.getId())).thenReturn(true);
//
//        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
//        verify(branchRepository, never()).save(any());
//    }
//
//    @Test
//    void updateBranch_shouldThrow_whenTelephoneExists() {
//        BranchUpdateRequestDto branch = BranchTestDataFactory.buildUpdateBranchRequest("Unique Name","UNIQUE001-1","unique@ntc.gov.lk","0112345678");
//        branch.setId(2);
//
//        when(branchRepository.existsByTelephoneAndIdNot(branch.getTelephone(), branch.getId())).thenReturn(true);
//
//        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
//        verify(branchRepository, never()).save(any());
//    }
//
//    @Test
//    void updateBranch_shouldSucceed_whenAllFieldsUnique() {
//        BranchUpdateRequestDto branchUpdateRequest = BranchTestDataFactory.buildUpdateBranchRequest("Colombo Branch-Head","UNIQUE001-1","0112345678","colombohead@ntc.gov.lk");
//        branchUpdateRequest.setId(1);
//
//        Branch branchEntity = branchMapper.toEntity(branchUpdateRequest);
//
//
//        when(branchRepository.existsByCodeAndIdNot(branchUpdateRequest.getCode(), branchUpdateRequest.getId())).thenReturn(false);
//        when(branchRepository.existsByNameAndIdNot(branchUpdateRequest.getName(), branchUpdateRequest.getId())).thenReturn(false);
//        when(branchRepository.existsByEmailAndIdNot(branchUpdateRequest.getEmail(), branchUpdateRequest.getId())).thenReturn(false);
//        when(branchRepository.existsByTelephoneAndIdNot(branchUpdateRequest.getTelephone(), branchUpdateRequest.getId())).thenReturn(false);
//        when(branchRepository.save(branchEntity)).thenReturn(branchEntity);
//
//        BranchDetailResponseDto result = branchService.updateBranch(branchUpdateRequest);
//
//        assertBranch(result, result.getName(), result.getCode(), result.getBranchstatus().getId());
//        verify(branchRepository).save(branchEntity);
//    }
//
//
//    // ────────────── DELETE Test ──────────────
//
//    @Test
//    void deleteBranch_shouldThrow_whenBranchNotFound() {
//        when(branchRepository.findById(99)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> branchService.deleteBranch(99));
//
//        verify(branchRepository, never()).delete(any());
//    }
//
//    @Test
//    void deleteBranch_shouldMarkStatusAsClosed() {
//
//        Branchtype branchtype = BranchTestDataFactory.buildBranchType(1, "Head");
//        Branchstatus branchstatus = BranchTestDataFactory.buildBranchStatus(1, "Active");
//
//        Branch branch = BranchTestDataFactory. buildBranch(
//                "Colombo Branch", "CLB0001-1", "colombo@ntc.gov.lk", "0112345678",
//                fixedDate, branchtype, branchstatus
//        );
//        branch.setId(1);
//
//        when(branchRepository.findById(1)).thenReturn((Optional<Branch>) Optional.of(branch));
//
//        branchService.deleteBranch(1);
//
//        verify(branchRepository).delete(branch);
//    }


    // ────────────── HELPERS ──────────────

    private void assertBranch(BranchDetailResponseDto branch, String expectedName, String expectedCode, Integer expectedStatusId) {
        assertEquals(expectedName, branch.getName());
        assertEquals(expectedCode, branch.getCode());
        assertEquals(expectedStatusId, branch.getBranchstatus().getId());
        assertEquals(fixedDate, branch.getDocreated());
    }

    private void assertBranchExists(List<BranchDetailResponseDto> branches, String name) {
        assertTrue(branches.stream().anyMatch(b -> b.getName().equals(name)));
    }


}
