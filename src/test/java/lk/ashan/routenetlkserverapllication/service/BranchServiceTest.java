package lk.ashan.routenetlkserverapllication.service;

import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequest;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchFullResponse;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequest;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchtype;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.util.factory.BranchTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    private Date fixedDate;
    private List<Branch> mockBranches;

    @Mock
    private BranchRepository branchRepository;

    private final BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);

    private BranchService branchService;

    @BeforeEach
    void setUp() {
        fixedDate = BranchTestDataFactory.getFixedDate();
        mockBranches = BranchTestDataFactory.buildMockBranches(fixedDate);
        branchService = new BranchService(branchRepository, branchMapper);
    }


    // ────────────── RETRIEVE TESTS ──────────────

    @Test
    void getBranches_shouldReturnAllBranches() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        List<BranchFullResponse> result = branchService.getBranches();

        assertEquals(2, result.size());
        assertBranchExists(result, "Colombo Branch");
        assertBranchExists(result, "Ratnapura Branch");

        verify(branchRepository, times(1)).findAll();
    }

    @Test
    void searchBranch_shouldFilterByName() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchname", "Colombo");

        List<BranchFullResponse> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Colombo Branch", "CLB0001-1", 1);
    }

    @Test
    void searchBranch_shouldFilterByCode() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchcode", "RAT0007-1");

        List<BranchFullResponse> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Ratnapura Branch", "RAT0007-1", 2);
    }

    @Test
    void searchBranch_shouldFilterByStatusId() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchstatusid", "1");

        List<BranchFullResponse> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
    }

    @Test
    void searchBranch_shouldApplyMultipleFiltersTogether() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchname", "Colombo");
        params.put("branchstatusid", "1");

        List<BranchFullResponse> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Colombo Branch", "CLB0001-1", 1);
    }

    @Test
    void searchBranch_shouldReturnMatchingWhenAllFiltersMatch() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> emptyParams = new HashMap<>();
        List<BranchFullResponse> result = branchService.searchBranch(emptyParams);

        assertEquals(2, result.size());
    }


    // ────────────── CREATE TESTS ──────────────

    @Test
    void createBranch_shouldThrow_whenNameExists() {
        BranchCreateRequest branch = BranchTestDataFactory.buildCreateBranchRequest("Colombo Branch", "UNIQUE001-1", "unique@ntc.gov.lk", "0119998888");
        when(branchRepository.existsByName(branch.getName())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenCodeExists() {
        BranchCreateRequest branch = BranchTestDataFactory.buildCreateBranchRequest("Unique Branch", "CLB0001-1", "unique@ntc.gov.lk", "0119998888");
        when(branchRepository.existsByCode(branch.getCode())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenEmailExists() {
        BranchCreateRequest branch = BranchTestDataFactory.buildCreateBranchRequest("Unique Branch", "UNIQUE001-1", "colombo@ntc.gov.lk", "0119998888");
        when(branchRepository.existsByEmail(branch.getEmail())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenTelephoneExists() {
        BranchCreateRequest branch = BranchTestDataFactory.buildCreateBranchRequest("Unique Branch", "UNIQUE001-1", "unique@ntc.gov.lk", "0112345678");
        when(branchRepository.existsByTelephone(branch.getTelephone())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldSucceed_whenAllUnique() {
        BranchCreateRequest branchCreateRequest = BranchTestDataFactory.buildCreateBranchRequest("Unique Name", "UNIQUE001-1", "unique@ntc.gov.lk", "0112223344");

        Branch branchEntity = branchMapper.toBranchEntity(branchCreateRequest);

        when(branchRepository.existsByName(branchCreateRequest.getName())).thenReturn(false);
        when(branchRepository.existsByCode(branchCreateRequest.getCode())).thenReturn(false);
        when(branchRepository.existsByEmail(branchCreateRequest.getEmail())).thenReturn(false);
        when(branchRepository.existsByTelephone(branchCreateRequest.getTelephone())).thenReturn(false);
        when(branchRepository.save(branchEntity)).thenReturn(branchEntity);

        BranchFullResponse created = branchService.createBranch(branchCreateRequest);

        assertNotNull(created);
        assertEquals("Unique Name", created.getName());
        assertEquals("UNIQUE001-1", created.getCode());
        assertEquals("unique@ntc.gov.lk", created.getEmail());
        assertEquals("0112223344", created.getTelephone());
    }


    // ────────────── UPDATE TESTS ──────────────

    @Test
    void updateBranch_shouldThrow_whenNameExists() {
        BranchUpdateRequest branch = BranchTestDataFactory.buildUpdateBranchRequest("Colombo Branch","UNIQUE001-1","unique@ntc.gov.lk","0112223344");
        branch.setId(2);

        when(branchRepository.existsByNameAndIdNot(branch.getName(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldThrow_whenCodeExists() {
        BranchUpdateRequest branch = BranchTestDataFactory.buildUpdateBranchRequest("Unique Name","CLB0001-1","unique@ntc.gov.lk","0112223344");
        branch.setId(2);

        when(branchRepository.existsByCodeAndIdNot(branch.getCode(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldThrow_whenEmailExists() {
        BranchUpdateRequest branch = BranchTestDataFactory.buildUpdateBranchRequest("Unique Name","UNIQUE001-1","colombo@ntc.gov.lk","0112223344");
        branch.setId(2);

        when(branchRepository.existsByEmailAndIdNot(branch.getEmail(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldThrow_whenTelephoneExists() {
        BranchUpdateRequest branch = BranchTestDataFactory.buildUpdateBranchRequest("Unique Name","UNIQUE001-1","unique@ntc.gov.lk","0112345678");
        branch.setId(2);

        when(branchRepository.existsByTelephoneAndIdNot(branch.getTelephone(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldSucceed_whenAllFieldsUnique() {
        BranchUpdateRequest branchUpdateRequest = BranchTestDataFactory.buildUpdateBranchRequest("Colombo Branch-Head","UNIQUE001-1","0112345678","colombohead@ntc.gov.lk");
        branchUpdateRequest.setId(1);

        Branch branchEntity = branchMapper.toBranchEntity(branchUpdateRequest);


        when(branchRepository.existsByCodeAndIdNot(branchUpdateRequest.getCode(), branchUpdateRequest.getId())).thenReturn(false);
        when(branchRepository.existsByNameAndIdNot(branchUpdateRequest.getName(), branchUpdateRequest.getId())).thenReturn(false);
        when(branchRepository.existsByEmailAndIdNot(branchUpdateRequest.getEmail(), branchUpdateRequest.getId())).thenReturn(false);
        when(branchRepository.existsByTelephoneAndIdNot(branchUpdateRequest.getTelephone(), branchUpdateRequest.getId())).thenReturn(false);
        when(branchRepository.save(branchEntity)).thenReturn(branchEntity);

        BranchFullResponse result = branchService.updateBranch(branchUpdateRequest);

        assertBranch(result, result.getName(), result.getCode(), result.getBranchstatus().getId());
        verify(branchRepository).save(branchEntity);
    }


    // ────────────── DELETE Test ──────────────

    @Test
    void deleteBranch_shouldThrow_whenBranchNotFound() {
        when(branchRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> branchService.deleteBranch(99));

        verify(branchRepository, never()).delete(any());
    }

    @Test
    void deleteBranch_shouldMarkStatusAsClosed() {

        Branchtype branchtype = BranchTestDataFactory.buildBranchType(1, "Head");
        Branchstatus branchstatus = BranchTestDataFactory.buildBranchStatus(1, "Active");

        Branch branch = BranchTestDataFactory. buildBranch(
                "Colombo Branch", "CLB0001-1", "colombo@ntc.gov.lk", "0112345678",
                fixedDate, branchtype, branchstatus
        );
        branch.setId(1);

        when(branchRepository.findById(1)).thenReturn((Optional<Branch>) Optional.of(branch));

        branchService.deleteBranch(1);

        verify(branchRepository).delete(branch);
    }


    // ────────────── HELPERS ──────────────

    private void assertBranch(BranchFullResponse branch, String expectedName, String expectedCode, Integer expectedStatusId) {
        assertEquals(expectedName, branch.getName());
        assertEquals(expectedCode, branch.getCode());
        assertEquals(expectedStatusId, branch.getBranchstatus().getId());
        assertEquals(fixedDate, branch.getDocreated());
    }

    private void assertBranchExists(List<BranchFullResponse> branches, String name) {
        assertTrue(branches.stream().anyMatch(b -> b.getName().equals(name)));
    }


}
