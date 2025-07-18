package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.Branch;
import lk.ashan.ntcserverapllication.model.entity.Branchstatus;
import lk.ashan.ntcserverapllication.model.entity.Branchtype;
import lk.ashan.ntcserverapllication.repository.BranchRepository;
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

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private Date fixedDate;
    private List<Branch> mockBranches;

    @BeforeEach
    void setUp() {
        fixedDate = Date.valueOf("2025-07-14");
        mockBranches = buildMockBranches();
    }


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
        params.put("brachstatusid", "1");

        List<Branch> result = branchService.searchBranch(params);

        assertEquals(2, result.size());
    }

    @Test
    void searchBranch_shouldApplyMultipleFiltersTogether() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> params = new HashMap<>();
        params.put("branchname", "Colombo");
        params.put("brachstatusid", "1");

        List<Branch> result = branchService.searchBranch(params);

        assertEquals(1, result.size());
        assertBranch(result.get(0), "Colombo Branch", "CLB0001", 1);
    }

    @Test
    void searchBranch_shouldReturnAllWhenParamsEmpty() {
        when(branchRepository.findAll()).thenReturn(mockBranches);

        HashMap<String, String> emptyParams = new HashMap<>();
        List<Branch> result = branchService.searchBranch(emptyParams);

        assertEquals(2, result.size());
    }

    private void assertBranch(Branch branch, String expectedName, String expectedCode, int expectedStatusId) {
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
                .branchstatus(new Branchstatus(1,"Active"))
                .branchcoverages(Collections.emptyList())
                .build();

        return List.of(colomboBranch, ratnapuraBranch);
    }
}
