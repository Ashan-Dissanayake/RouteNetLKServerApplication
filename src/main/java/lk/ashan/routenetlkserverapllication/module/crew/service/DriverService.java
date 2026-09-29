package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.criteria.Predicate;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.DriverMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.LicenseCategory;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.crew.validation.DriverValidator;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lk.ashan.routenetlkserverapllication.shared.specification.CommonPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class for managing Driver entities.
 * Provides methods for retrieving, creating, and updating drivers.
 */
@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final NumberGeneratorService numberGeneratorService;
    private final RouteFamiliarityLevelService routeFamiliarityLevelService;
    private final CrewStatusService crewStatusService;
    private final LicenseCategoryService licenseCategoryService;
    private final DriverMapper driverMapper;
    private final DriverValidator driverValidator;

    /**
     * Retrieves all drivers.
     *
     * @return a list of DriverDetailResponseDto containing details of all drivers.
     */
    @Transactional(readOnly = true)
    public List<DriverDetailResponseDto> getDrivers() {
        return driverMapper.toDtoList(driverRepository.findAll());
    }

    /**
     * Searches for drivers based on the provided parameters.
     *
     * @param params a map containing search parameters such as "sslicensenumber",
     *               "sscrewstatus", and "ssroutefamilitylevel".
     * @return a list of {@link DriverDetailResponseDto} matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<DriverDetailResponseDto> searchDriver(@NotNull HashMap<String, String> params) {

        Specification<Driver> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String number = params.get("sslicensenumber");
            String crewStatusId = params.get("sscrewstatus");
            String routeFamiliarityLevelId = params.get("ssroutefamilitylevel");

            if (number != null && !number.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("licensenumber")),
                                number.toLowerCase()
                        )
                );
            }

            if (crewStatusId != null && !crewStatusId.isBlank()) {
                predicates.add(
                        CommonPredicates.hasId(
                                root,
                                criteriaBuilder,
                                "crewstatus",
                                Integer.parseInt(crewStatusId)
                        )
                );
            }

            if (routeFamiliarityLevelId != null && !routeFamiliarityLevelId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("routefamiliaritylevel").get("id"),
                                Integer.parseInt(routeFamiliarityLevelId)
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        List<Driver> drivers =
                driverRepository.findAll(specification);

        return driverMapper.toDtoList(drivers);
    }

    /**
     * Creates a new driver.
     *
     * @param dto the DriverCreateRequestDto containing details of the driver to be created.
     * @return the created DriverDetailResponseDto.
     * @throws ValidationException if the driver's crew status is not "ELIGIBLE" or route familiarity is not "LOW".
     */
    @Transactional
    public DriverDetailResponseDto createDriver(@NotNull DriverCreateRequestDto dto) {

        driverValidator.validateCreate(dto);

        Driver driver = driverMapper.toEntity(dto);
        driver.setNumber(numberGeneratorService.nextDriverNumber());

        return driverMapper.toDto(
                driverRepository.save(driver));
    }

    /**
     * Updates an existing driver.
     *
     * @param dto the DriverUpdateRequestDto containing updated details of the driver.
     * @return the updated DriverDetailResponseDto.
     * @throws ResourceNotFoundException if the driver to be updated is not found.
     */
    @Transactional
    public DriverDetailResponseDto updateDriver(@NotNull DriverUpdateRequestDto dto) {

        Driver existingDriver = driverRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver not found"));

        driverValidator.validateUpdate(existingDriver, dto);

        Driver entity =
                driverMapper.updateEntityFromDto(dto, existingDriver);

        if (dto.getRoutefamiliaritylevel().getId() != null) {
            RouteFamiliarityLevel level =
                    routeFamiliarityLevelService.getById(
                            dto.getRoutefamiliaritylevel().getId());

            entity.setRoutefamiliaritylevel(level);
        }

        if (dto.getLicensecategory().getId() != null) {
            LicenseCategory category =
                    licenseCategoryService.getById(
                            dto.getLicensecategory().getId());

            entity.setLicensecategory(category);
        }

        if (dto.getCrewstatus().getId() != null) {
            CrewStatus status =
                    crewStatusService.getById(
                            dto.getCrewstatus().getId());

            entity.setCrewstatus(status);
        }

        return driverMapper.toDto(entity);
    }

    @Transactional
    public void updateCrewStatus(
            Integer employeeId,
            CrewStatus status
    ) {

        Driver driver = driverRepository
                .findByEmployee_Id(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Driver not found for employee ID: " + employeeId
                        ));

        driver.setCrewstatus(status);
    }

}
