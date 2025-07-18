package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.Branchstatus;
import lk.ashan.ntcserverapllication.repository.BranchstatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchstatusServiceTest {

    @Mock
    private BranchstatusRepository branchstatusRepository;

    @InjectMocks
    private BranchstatusService branchstatusService;

    @Test
    void getBranchstatuss() {

        List<Branchstatus> mockBranchstatuses = Arrays.asList(
                new Branchstatus(1, "Active"),
                new Branchstatus(2, "Inactive"),
                new Branchstatus(3, "Closed")

        );

        when(branchstatusRepository.findAll()).thenReturn(mockBranchstatuses);

        List<Branchstatus> result = branchstatusService.getBranchstatuss();

        assertEquals(3, result.size());

        assertEquals("Active", result.get(0).getName());
        assertEquals("Inactive", result.get(1).getName());
        assertEquals("Closed", result.get(2).getName());

    }
}
