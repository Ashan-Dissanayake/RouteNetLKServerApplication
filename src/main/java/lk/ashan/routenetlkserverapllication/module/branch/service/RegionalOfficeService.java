package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.RegionalOfficeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalOfficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.repository.RegionalOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionalOfficeService {

    private final RegionalOfficeRepository regionalofficeRepository;
    private final RegionalOfficeMapper regionalofficeMapper;

    public List<RegionalOfficeDto> getRegionalOffices() {
        return regionalofficeMapper.toDtoList(regionalofficeRepository.findAll());
    }
}
