package lk.ashan.routenetlkserverapllication.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchstatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchstatusMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchstatusRepository;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchstatusService;
import lk.ashan.routenetlkserverapllication.util.factory.BranchTestDataFactory;
import lk.ashan.routenetlkserverapllication.util.factory.EntityFactory;
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
    void getBranchstatuses_shouldReturnAllBranchstatuses() {

        List<Branchstatus> mockBranchstatuses = Arrays.asList(
                EntityFactory.branchStatus(1, "Active"),
                EntityFactory.branchStatus(2, "Inactive"),
                EntityFactory.branchStatus(3, "Closed")
        );

        when(branchstatusRepository.findAll()).thenReturn(mockBranchstatuses);

        List<BranchstatusDto> result = branchstatusService.getBranchstatuses();

        assertEquals(3, result.size());

        assertEquals("Active", result.get(0).getName());
        assertEquals("Inactive", result.get(1).getName());
        assertEquals("Closed", result.get(2).getName());

    }
}
