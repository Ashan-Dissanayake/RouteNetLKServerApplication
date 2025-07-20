package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.District;
import lk.ashan.ntcserverapllication.model.entity.Province;
import lk.ashan.ntcserverapllication.repository.DistrictRepository;
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
class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private DistrictService districtService;

    @Test
    void getDistricts() {

        Province western = new Province(1, "Western");
        Province northern = new Province(4, "Northern");
        Province uva = new Province(8, "Uva");

        List<District> mockDistrictes = Arrays.asList(
                new District(1, "Colombo",western),
                new District(12, "Mullaitivu",northern),
                new District(23, "Monaragala",uva)
        );

        when(districtRepository.findAll()).thenReturn(mockDistrictes);

        List<District> result = districtService.getDistricts();

        assertEquals(3, result.size());

        assertEquals("Colombo", result.get(0).getName());
        assertEquals("Mullaitivu", result.get(1).getName());
        assertEquals("Monaragala", result.get(2).getName());

        assertEquals("Western", result.get(0).getProvince().getName());
        assertEquals("Northern", result.get(1).getProvince().getName());
        assertEquals("Uva", result.get(2).getProvince().getName());

    }
}
