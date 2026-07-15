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

/**
 * Service class for managing Regional Offices.
 * Provides methods to retrieve regional office data.
 */
@Service
@RequiredArgsConstructor
public class RegionalOfficeService {

    private final RegionalOfficeRepository regionalOfficeRepository;
    private final RegionalOfficeMapper regionalOfficeMapper;

    /**
     * Retrieves a list of all regional offices.
     *
     * @return a list of {@link RegionalOfficeDto} objects representing all regional offices.
     */
    @Transactional(readOnly = true)
    public List<RegionalOfficeDto> getRegionalOffices() {
        return regionalOfficeMapper.toDtoList(regionalOfficeRepository.findAll());
    }

    /**
     * Retrieves a regional office by its ID.
     *
     * @param id the ID of the regional office to retrieve.
     * @return the {@link RegionalOffice} object corresponding to the given ID.
     * @throws ResourceNotFoundException if no regional office is found with the given ID.
     */
    @Transactional(readOnly = true)
    public RegionalOffice getById(Integer id) {
        return regionalOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Regional office not found"
                ));
    }
}
