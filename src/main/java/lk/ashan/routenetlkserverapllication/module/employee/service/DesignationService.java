package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.DesignationMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.repository.DesignationRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing designations.
 * Provides methods to retrieve designations and fetch a designation by its ID.
 */
@Service
@RequiredArgsConstructor
public class DesignationService {

    private final DesignationRepository designationRepository;
    private final DesignationMapper designationMapper;

    /**
     * Retrieves all designations.
     *
     * @return a list of {@link DesignationDto} objects representing all designations.
     */
    @Transactional(readOnly = true)
    public List<DesignationDto> getDesignations() {
        return designationMapper.toDtoList(designationRepository.findAll());
    }

    /**
     * Retrieves a designation by its ID.
     *
     * @param id the ID of the designation to retrieve.
     * @return the {@link Designation} object corresponding to the given ID.
     * @throws ResourceNotFoundException if no designation is found with the given ID.
     */
    @Transactional(readOnly = true)
    public Designation getById(Integer id) {
        return designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Designation not found"
                ));
    }

}
