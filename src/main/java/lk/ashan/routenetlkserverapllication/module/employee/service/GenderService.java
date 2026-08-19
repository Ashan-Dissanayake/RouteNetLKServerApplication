package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.GenderMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.GenderDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Gender;
import lk.ashan.routenetlkserverapllication.module.employee.repository.GenderRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Gender entities.
 * Provides methods to retrieve gender data.
 */
@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;
    private final GenderMapper genderMapper;

    /**
     * Retrieves a list of all genders.
     *
     * @return a list of GenderDto objects representing all genders.
     */
    @Transactional(readOnly = true)
    public List<GenderDto> getGenders() {
        return genderMapper.toDtoList(genderRepository.findAll());
    }

    /**
     * Retrieves a Gender entity by its ID.
     *
     * @param id the ID of the Gender entity to retrieve.
     * @return the Gender entity with the specified ID.
     * @throws ResourceNotFoundException if no Gender entity is found with the given ID.
     */
    @Transactional(readOnly = true)
    public Gender getById(Integer id) {
        return genderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gender not found"
                ));
    }
}
