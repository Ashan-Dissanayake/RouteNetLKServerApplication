package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalofficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.RegionalofficeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.RegionalofficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionalOfficeService {

    private final RegionalofficeRepository regionalofficeRepository;
    private final RegionalofficeMapper regionalofficeMapper;

    public List<RegionalofficeDto> getRegionalOffices() {
        return regionalofficeMapper.toDtoList(regionalofficeRepository.findAll());
    }
}
