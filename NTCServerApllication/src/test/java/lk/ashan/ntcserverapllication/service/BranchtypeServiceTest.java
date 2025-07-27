package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchtypeResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.BranchtypeMapper;
import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import lk.ashan.ntcserverapllication.module.branch.repository.BranchtypeRepository;
import lk.ashan.ntcserverapllication.module.branch.service.BranchtypeService;
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
class BranchtypeServiceTest {

    @Mock
    private BranchtypeRepository branchtypeRepository;

    private final BranchtypeMapper branchtypeMapper = Mappers.getMapper(BranchtypeMapper.class);

    private BranchtypeService branchtypeService;

    @BeforeEach
    void setUp() {
        branchtypeService = new BranchtypeService(branchtypeRepository, branchtypeMapper);
    }


    @Test
    void getBranchtypes() {

        List<Branchtype> mockBranchtypes = Arrays.asList(
                new Branchtype(1, "Head"),
                new Branchtype(2, "Region")
        );

        when(branchtypeRepository.findAll()).thenReturn(mockBranchtypes);

        List<BranchtypeResponse> result = branchtypeService.getBranchtypes();

        assertEquals(2, result.size());

        assertEquals("Head", result.get(0).getName());
        assertEquals("Region", result.get(1).getName());

    }
}
