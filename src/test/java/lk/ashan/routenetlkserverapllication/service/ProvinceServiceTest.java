package lk.ashan.routenetlkserverapllication.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.ProvinceDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Province;
import lk.ashan.routenetlkserverapllication.module.branch.repository.ProvinceRepository;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.ProvinceMapper;
import lk.ashan.routenetlkserverapllication.module.branch.service.ProvinceService;

import lk.ashan.routenetlkserverapllication.util.factory.EntityFactory;
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
    void getProvinces_shouldReturnAllProvinces() {
        List<Province> mockProvinces = Arrays.asList(
               EntityFactory.province(1, "Western"),
               EntityFactory.province(5, "Eastern")

        );
        when(provinceRepository.findAll()).thenReturn(mockProvinces);

        List<ProvinceDto> result = provinceService.getProvinces();

        assertEquals(2, result.size());
        assertEquals("Western", result.get(0).getName());
        assertEquals("Eastern", result.get(1).getName());
    }
}
