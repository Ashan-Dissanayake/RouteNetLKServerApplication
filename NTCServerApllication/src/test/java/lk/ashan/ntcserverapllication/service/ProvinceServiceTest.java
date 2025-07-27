package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.module.branch.model.Province;
import lk.ashan.ntcserverapllication.module.branch.repository.ProvinceRepository;
import lk.ashan.ntcserverapllication.module.branch.dto.ProvinceResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.ProvinceMapper;
import lk.ashan.ntcserverapllication.module.branch.service.ProvinceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProvinceServiceTest {

    @Mock
    private ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper = Mappers.getMapper(ProvinceMapper.class);

    private ProvinceService provinceService;

    @BeforeEach
    void setUp() {
        provinceService = new ProvinceService(provinceRepository, provinceMapper);
    }

    @Test
    void getProvinces() {
        List<Province> mockProvinces = Arrays.asList(
                new Province(1, "Western"),
                new Province(5, "Eastern")
        );
        when(provinceRepository.findAll()).thenReturn(mockProvinces);

        List<ProvinceResponse> result = provinceService.getProvinces();

        assertEquals(2, result.size());
        assertEquals("Western", result.get(0).getName());
        assertEquals("Eastern", result.get(1).getName());
    }
}
