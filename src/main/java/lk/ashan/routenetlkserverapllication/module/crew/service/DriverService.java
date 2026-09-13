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
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.DriverContextBuilder;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.DriverValidationContext;
import lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy.DriverValidationStrategy;
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

    private final List<DriverValidationStrategy> validationStrategies;
    private final DriverContextBuilder driverContextBuilder;

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

        DriverValidationContext context = driverContextBuilder.buildForCreate(dto);
        validationStrategies.forEach(s -> s.validateCreate(context));

        if (!dto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new ValidationException("New driver must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName().equalsIgnoreCase("Low")) {
            throw new ValidationException("New driver route familiarity must have 'LOW'");
        }

        Driver driver = driverMapper.toEntity(dto);
        driver.setNumber(numberGeneratorService.nextDriverNumber());
        return driverMapper.toDto(driverRepository.save(driver));
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
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        DriverValidationContext context = driverContextBuilder.buildForUpdate(dto, existingDriver);
        validationStrategies.forEach(s -> s.validateUpdate(context));

        Driver entity = driverMapper.updateEntityFromDto(dto, existingDriver);

        if (dto.getRoutefamiliaritylevel().getId() != null) {
            RouteFamiliarityLevel targetRouteFamiliarityLevel = routeFamiliarityLevelService.getById(dto.getRoutefamiliaritylevel().getId());
            entity.setRoutefamiliaritylevel(targetRouteFamiliarityLevel);
        }

        if (dto.getLicensecategory().getId() != null) {
            LicenseCategory targetLicenseCategory = licenseCategoryService.getById(dto.getLicensecategory().getId());
            entity.setLicensecategory(targetLicenseCategory);
        }

        if (dto.getCrewstatus().getId() != null) {
            CrewStatus targetStatus = crewStatusService.getById(dto.getCrewstatus().getId());
            entity.setCrewstatus(targetStatus);
        }

        return driverMapper.toDto(entity);
    }

}
