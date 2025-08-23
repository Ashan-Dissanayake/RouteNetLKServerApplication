package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.module.branch.dto.DistrictResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.DistrictMapper;
import lk.ashan.ntcserverapllication.module.branch.model.District;
import lk.ashan.ntcserverapllication.module.branch.model.Province;
import lk.ashan.ntcserverapllication.module.branch.repository.DistrictRepository;
import lk.ashan.ntcserverapllication.module.branch.service.DistrictService;
import lk.ashan.ntcserverapllication.util.factory.BranchTestDataFactory;
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
class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;

    private final DistrictMapper districtMapper = Mappers.getMapper(DistrictMapper.class);

    private DistrictService districtService;

    @BeforeEach
    void setUp() {
        districtService = new DistrictService(districtRepository, districtMapper);
    }


    @Test
    void getDistricts_shouldReturnAllDistricts() {

        Province western = BranchTestDataFactory.buildProvince(1, "Western");
        Province northern = BranchTestDataFactory.buildProvince(4, "Northern");
        Province uva = BranchTestDataFactory.buildProvince(8, "Uva");

        List<District> mockDistrictes = Arrays.asList(
                BranchTestDataFactory.buildDistrictWithProvince(1, "Colombo",western),
                BranchTestDataFactory.buildDistrictWithProvince(12, "Mullaitivu",northern),
                BranchTestDataFactory.buildDistrictWithProvince(23, "Monaragala",uva)
        );

        when(districtRepository.findAll()).thenReturn(mockDistrictes);

        List<DistrictResponse> result = districtService.getDistricts();

        assertEquals(3, result.size());

        assertEquals("Colombo", result.get(0).getName());
        assertEquals("Mullaitivu", result.get(1).getName());
        assertEquals("Monaragala", result.get(2).getName());

        assertEquals("Western", result.get(0).getProvince().getName());
        assertEquals("Northern", result.get(1).getProvince().getName());
        assertEquals("Uva", result.get(2).getProvince().getName());

    }
}
