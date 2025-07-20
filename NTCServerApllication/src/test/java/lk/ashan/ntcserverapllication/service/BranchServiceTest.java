package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.exception.ResourceExistsException;
import lk.ashan.ntcserverapllication.exception.ResourceNotFoundException;
import lk.ashan.ntcserverapllication.module.branch.model.Branch;
import lk.ashan.ntcserverapllication.module.branch.model.Branchstatus;
import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import lk.ashan.ntcserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.ntcserverapllication.module.branch.service.BranchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private BranchService branchService;

    @BeforeEach
    void setUp() {
        fixedDate = Date.valueOf("2025-07-14");
        mockBranches = buildMockBranches();
    }


    // ────────────── RETRIEVE TESTS ──────────────

    @Test
    void getBranches_shouldReturnAllBranches() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        List<Branch> result = branchService.getBranches();

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

        List<Branch> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Colombo Branch", "CLB0001", 1);
    }

    @Test
    void searchBranch_shouldFilterByCode() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchcode", "RAT0007");

        List<Branch> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Ratnapura Branch", "RAT0007", 1);
    }

    @Test
    void searchBranch_shouldFilterByStatusId() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchstatusid", "1");

        List<Branch> result = branchService.searchBranch(params);

        assertEquals(2, result.size());
    }

    @Test
    void searchBranch_shouldApplyMultipleFiltersTogether() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchname", "Colombo");
        params.put("branchstatusid", "1");

        List<Branch> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Colombo Branch", "CLB0001", 1);
    }

    @Test
    void searchBranch_shouldReturnMatchingWhenAllFiltersMatch() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> emptyParams = new HashMap<>();
        List<Branch> result = branchService.searchBranch(emptyParams);

        assertEquals(2, result.size());
    }


    // ────────────── CREATE TESTS ──────────────

    @Test
    void createBranch_shouldThrow_whenNameExists() {
        Branch branch = buildBranch("Colombo Branch", "UNIQUE001", "unique@ntc.gov.lk", "0119998888");
        when(branchRepository.existsByName(branch.getName())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenCodeExists() {
        Branch branch = buildBranch("Unique Branch", "CLB0001", "unique@ntc.gov.lk", "0119998888");
        when(branchRepository.existsByCode(branch.getCode())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenEmailExists() {
        Branch branch = buildBranch("Unique Branch", "UNIQUE001", "colombo@ntc.gov.lk", "0119998888");
        when(branchRepository.existsByEmail(branch.getEmail())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldThrow_whenTelephoneExists() {
        Branch branch = buildBranch("Unique Branch", "UNIQUE001", "unique@ntc.gov.lk", "0112345678");
        when(branchRepository.existsByTelephone(branch.getTelephone())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.createBranch(branch));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_shouldSucceed_whenAllUnique() {
        Branch branch = buildBranch("Unique Name", "UNI001", "unique@ntc.gov.lk", "0112223344");

        when(branchRepository.existsByName(branch.getName())).thenReturn(false);
        when(branchRepository.existsByCode(branch.getCode())).thenReturn(false);
        when(branchRepository.existsByEmail(branch.getEmail())).thenReturn(false);
        when(branchRepository.existsByTelephone(branch.getTelephone())).thenReturn(false);
        when(branchRepository.save(branch)).thenReturn(branch);

        Branch created = branchService.createBranch(branch);

        assertNotNull(created);
        assertEquals("Unique Name", created.getName());
        assertEquals("UNI001", created.getCode());
        assertEquals("unique@ntc.gov.lk", created.getEmail());
        assertEquals("0112223344", created.getTelephone());
    }


    // ────────────── UPDATE TESTS ──────────────

    @Test
    void updateBranch_shouldThrow_whenNameExists() {
        Branch branch = buildBranch("Colombo Branch","UNI001","unique@ntc.gov.lk","0112223344");
        branch.setId(2);

        when(branchRepository.existsByNameAndIdNot(branch.getName(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldThrow_whenCodeExists() {
        Branch branch = buildBranch("Unique Name","CLB0001","unique@ntc.gov.lk","0112223344");
        branch.setId(2);

        when(branchRepository.existsByCodeAndIdNot(branch.getCode(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldThrow_whenEmailExists() {
        Branch branch = buildBranch("Unique Name","UNI001","colombo@ntc.gov.lk","0112223344");
        branch.setId(2);

        when(branchRepository.existsByEmailAndIdNot(branch.getEmail(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldThrow_whenTelephoneExists() {
        Branch branch = buildBranch("Unique Name","UNI001","unique@ntc.gov.lk","0112345678");
        branch.setId(2);

        when(branchRepository.existsByTelephoneAndIdNot(branch.getTelephone(), branch.getId())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> branchService.updateBranch(branch));
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranch_shouldSucceed_whenAllFieldsUnique() {
        Branch branch = buildBranch("Colombo Branch-Head","CLB0001","0112345678","colombohead@ntc.gov.lk");
        branch.setId(1);

        when(branchRepository.existsByCodeAndIdNot(branch.getCode(), branch.getId())).thenReturn(false);
        when(branchRepository.existsByNameAndIdNot(branch.getName(), branch.getId())).thenReturn(false);
        when(branchRepository.existsByEmailAndIdNot(branch.getEmail(), branch.getId())).thenReturn(false);
        when(branchRepository.existsByTelephoneAndIdNot(branch.getTelephone(), branch.getId())).thenReturn(false);
        when(branchRepository.save(branch)).thenReturn(branch);

        Branch result = branchService.updateBranch(branch);

        assertBranch(result, branch.getName(), branch.getCode(), branch.getBranchstatus().getId());
        verify(branchRepository).save(branch);
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
        Branch branch = buildBranch("Colombo Branch","CLB0001","colombo@ntc.gov.lk","0112345678");
        branch.setId(1);

        when(branchRepository.findById(1)).thenReturn(Optional.of(branch));

        branchService.deleteBranch(1);

        verify(branchRepository).delete(branch);
    }


    // ────────────── HELPERS ──────────────

    private void assertBranch(Branch branch, String expectedName, String expectedCode, Integer expectedStatusId) {
        assertEquals(expectedName, branch.getName());
        assertEquals(expectedCode, branch.getCode());
        assertEquals(expectedStatusId, branch.getBranchstatus().getId());
        assertEquals(fixedDate, branch.getDocreated());
    }

    private void assertBranchExists(List<Branch> branches, String name) {
        assertTrue(branches.stream().anyMatch(b -> b.getName().equals(name)));
    }

    private List<Branch> buildMockBranches() {
        Branch colomboBranch = Branch.builder()
                .id(1)
                .name("Colombo Branch")
                .code("CLB0001")
                .address("No. 123, Main Street, Colombo")
                .telephone("0112345678")
                .email("colombo@ntc.gov.lk")
                .docreated(fixedDate)
                .remarks("Head office and main HQ")
                .branchtype(new Branchtype(1,"Head"))
                .branchstatus(new Branchstatus(1,"Active"))
                .branchcoverages(Collections.emptyList())
                .build();

        Branch ratnapuraBranch = Branch.builder()
                .id(2)
                .name("Ratnapura Branch")
                .code("RAT0007")
                .address("No. 55, River Road, Ratnapura")
                .telephone("0452233445")
                .email("ratnapura@ntc.gov.lk")
                .docreated(fixedDate)
                .remarks("Sabaragamuwa Province branch")
                .branchtype(new Branchtype(2,"Region"))
                .branchstatus(new Branchstatus(2,"Inactive"))
                .branchcoverages(Collections.emptyList())
                .build();


        return List.of(colomboBranch, ratnapuraBranch);
    }

    private Branch buildBranch(String name, String code, String email, String telephone) {
        return Branch.builder()
                .name(name)
                .code(code)
                .email(email)
                .telephone(telephone)
                .address("123 Test Street")
                .remarks("Test branch")
                .docreated(fixedDate)
                .branchtype(new Branchtype(1, "Head"))
                .branchstatus(new Branchstatus(1, "Active"))
                .branchcoverages(Collections.emptyList())
                .build();
    }

}
