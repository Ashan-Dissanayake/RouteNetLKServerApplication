package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.RegionalOfficeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalOfficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.module.branch.repository.RegionalOfficeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
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

    @Transactional(readOnly = true)
    public RegionalOffice getById(Integer id) {
        return regionalOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Regional office not found"
                ));
    }
}
