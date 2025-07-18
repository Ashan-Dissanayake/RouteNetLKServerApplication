package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.Province;
import lk.ashan.ntcserverapllication.repository.ProvinceRepository;
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
class ProvinceServiceTest {

    @Mock
    private ProvinceRepository provinceRepository;

    @InjectMocks
    private ProvinceService provinceService;

    @Test
    void getProvinces() {

        List<Province> mockProvinces = Arrays.asList(
                new Province(1, "Western"),
                new Province(5, "Eastern")
        );

        when(provinceRepository.findAll()).thenReturn(mockProvinces);

        List<Province> result = provinceService.getProvinces();

        assertEquals(2, result.size());

        assertEquals("Western", result.get(0).getName());
        assertEquals("Eastern", result.get(1).getName());

    }
}
