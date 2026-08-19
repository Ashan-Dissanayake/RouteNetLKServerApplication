package lk.ashan.routenetlkserverapllication.module.grn.service;

import lk.ashan.routenetlkserverapllication.module.grn.mapper.GrnStatusMapper;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnStatusDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing GRN (Goods Received Note) statuses.
 * Provides methods to retrieve GRN statuses by ID, name, or all statuses.
 */
@Service
@RequiredArgsConstructor
public class GrnStatusService {

    private final GrnStatusRepository grnStatusRepository;
    private final GrnStatusMapper grnStatusMapper;

    /**
     * Retrieves all GRN statuses.
     *
     * @return a list of GRN statuses as DTOs.
     */
    @Transactional(readOnly = true)
    public List<GrnStatusDto> getGrnStatuses() {
        return grnStatusMapper.toDtoList(grnStatusRepository.findAll());
    }

    /**
     * Retrieves a GRN status by its ID.
     *
     * @param id the ID of the GRN status to retrieve.
     * @return the GRN status entity.
     * @throws ResourceNotFoundException if no GRN status is found with the given ID.
     */
    @Transactional(readOnly = true)
    public GrnStatus getById(Integer id) {
        return grnStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    /**
     * Retrieves a GRN status by its name.
     *
     * @param name the name of the GRN status to retrieve.
     * @return the GRN status entity.
     * @throws ResourceNotFoundException if no GRN status is found with the given name.
     */
    @Transactional(readOnly = true)
    public GrnStatus getByName(String name) {
        return grnStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
