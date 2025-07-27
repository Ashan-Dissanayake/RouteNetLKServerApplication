package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchstatusResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.BranchstatusMapper;
import lk.ashan.ntcserverapllication.module.branch.model.Branchstatus;
import lk.ashan.ntcserverapllication.module.branch.repository.BranchstatusRepository;
import lk.ashan.ntcserverapllication.module.branch.service.BranchstatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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

    private final BranchstatusMapper branchstatusMapper = Mappers.getMapper(BranchstatusMapper.class);

    private BranchstatusService branchstatusService;

    @BeforeEach
    void setUp() {
        branchstatusService = new BranchstatusService(branchstatusRepository, branchstatusMapper);
    }

    @Test
    void getBranchstatuss() {

        List<Branchstatus> mockBranchstatuses = Arrays.asList(
                new Branchstatus(1, "Active"),
                new Branchstatus(2, "Inactive"),
                new Branchstatus(3, "Closed")

        );

        when(branchstatusRepository.findAll()).thenReturn(mockBranchstatuses);

        List<BranchstatusResponse> result = branchstatusService.getBranchstatuses();

        assertEquals(3, result.size());

        assertEquals("Active", result.get(0).getName());
        assertEquals("Inactive", result.get(1).getName());
        assertEquals("Closed", result.get(2).getName());

    }
}
