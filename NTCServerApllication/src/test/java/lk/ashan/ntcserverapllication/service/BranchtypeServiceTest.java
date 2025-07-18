package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.Branchtype;
import lk.ashan.ntcserverapllication.repository.BranchtypeRepository;
import org.junit.jupiter.api.BeforeEach;
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
class BranchtypeServiceTest {

    @Mock
    private BranchtypeRepository branchtypeRepository;

    @InjectMocks
    private BranchtypeService branchtypeService;

    @Test
    void getBranchtypes() {

        List<Branchtype> mockBranchtypes = Arrays.asList(
                new Branchtype(1, "Head"),
                new Branchtype(2, "Region")
        );

        when(branchtypeRepository.findAll()).thenReturn(mockBranchtypes);

        List<Branchtype> result = branchtypeService.getBranchtypes();

        assertEquals(2, result.size());

        assertEquals("Head", result.get(0).getName());
        assertEquals("Region", result.get(1).getName());

    }
}
