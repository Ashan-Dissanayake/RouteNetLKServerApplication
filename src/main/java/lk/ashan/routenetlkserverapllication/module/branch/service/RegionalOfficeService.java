package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.RegionalOfficeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalOfficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.repository.RegionalOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionalOfficeService {

    private final RegionalOfficeRepository regionalOfficeRepository;
    private final RegionalOfficeMapper regionalOfficeMapper;

    @Transactional(readOnly = true)
    public List<RegionalOfficeDto> getRegionalOffices() {
        return regionalOfficeMapper.toDtoList(regionalOfficeRepository.findAll());
    }

}
