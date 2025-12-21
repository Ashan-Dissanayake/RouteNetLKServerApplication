package lk.ashan.routenetlkserverapllication.module.driver.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.driver.dto.*;
import lk.ashan.routenetlkserverapllication.module.driver.mapper.DriverMapper;
import lk.ashan.routenetlkserverapllication.module.driver.model.Driver;
import lk.ashan.routenetlkserverapllication.module.driver.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public List<DriverDetailResponseDto> getDrivers(){
       return driverMapper.toDtoList(driverRepository.findAll());
    }

    public List<DriverDetailResponseDto> searchDriver(@NotNull HashMap<String, String> params) {

        String number = params.get("ssnumber");
        String crewStatusId = params.get("sscrewstatus");
        String routeFamiliarityLevelId = params.get("ssroutefamilitylevel");

        Stream<Driver> driverStream = driverRepository.findAll().stream();

        if (number != null)
            driverStream = driverStream.filter(d->d.getNumber().equalsIgnoreCase(number));
        if (crewStatusId != null)
            driverStream = driverStream.filter(d->d.getCrewstatus().getId()==Integer.parseInt(crewStatusId));
        if (routeFamiliarityLevelId != null)
            driverStream = driverStream.filter(d -> d.getRoutefamiliaritylevel().getId()== Integer.parseInt(routeFamiliarityLevelId));

        return driverMapper.toDtoList(driverStream.collect(Collectors.toList()));

    }

    public DriverDetailResponseDto createDriver(@Valid @NotNull DriverCreateRequestDto dto) {

        validateLicenseDates(dto.getDolicenseissued(), dto.getDolicenseexpired());
        validateMedicalDates(dto.getDomedicalissued(), dto.getDomedicalexpired());
        validateUniqueness(dto);

        if (!dto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new InvalidStatusException("New driver must have status 'ELIGIBLE'");
        }

        Driver driver = driverMapper.toEntity(dto);
        return driverMapper.toDto(driverRepository.save(driver));
    }

    public DriverDetailResponseDto updateDriver(@Valid @NotNull DriverUpdateRequestDto dto) {

        Driver existingDriver =  driverRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        validateLicenseCategoryChange(existingDriver,dto.getLicensecategory());
        validateUniqueness(dto);
        validateRouteFamiliarityTransition(existingDriver.getRoutefamiliaritylevel().getName(),dto.getRoutefamiliaritylevel().getName());
        validateLicenseDates(dto.getDolicenseissued(), dto.getDolicenseexpired());
        validateMedicalDates(dto.getDomedicalissued(), dto.getDomedicalexpired());

        Driver driver = driverMapper.toEntity(dto);
        return driverMapper.toDto(driverRepository.save(driver));
    }

    public void validateLicenseCategoryChange(Driver existingDriver, LicenseCategoryDto newCategory) {

        if (newCategory == null) {
            throw new IllegalArgumentException("New license category cannot be null");
        }

        // 1. If category NOT changed → do nothing
        if (existingDriver.getLicensecategory().getId().equals(newCategory.getId())) {
            return;
        }

        // 2. Category changed → license must be expired
        if (!existingDriver.getDolicenseexpired().isBefore(LocalDate.now())) {
            throw new BusinessRuleValidationException(
                    "License category can only be changed when the existing license is expired"
            );
        }
    }

    private static final Map<String, List<String>> VALID_ROUTE_UPGRADES = Map.of(
            "Low",    List.of("Medium"),
            "Medium", List.of("High"),
            "High",   List.of() // no upgrade from High
    );

    private void validateRouteFamiliarityTransition(String currentLevel, String newLevel) {

        if (currentLevel == null || newLevel == null) {
            throw new IllegalArgumentException("Route familiarity level cannot be null.");
        }

        currentLevel = currentLevel.trim();
        newLevel = newLevel.trim();

        if (currentLevel.equals(newLevel)) return;

        List<String> allowedUpgrades = VALID_ROUTE_UPGRADES.get(currentLevel);

        if (allowedUpgrades == null) {
            throw new IllegalArgumentException(
                    "Unknown route familiarity level: " + currentLevel
            );
        }

        // downgrade → always allowed
        if (!allowedUpgrades.contains(newLevel)) {
            // if downgrade or invalid upgrade
            if (VALID_ROUTE_UPGRADES.containsKey(newLevel)) {
                return; // downgrade → allowed
            }
            throw new InvalidStatusTransitionException(
                    "Invalid route familiarity transition from " + currentLevel + " to " + newLevel
            );
        }

    }


    private void validateLicenseDates(LocalDate issued, LocalDate expiry) {
        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleValidationException("License issued date cannot be in the future");
        }
        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleValidationException("License expiry must be after issued date");
        }

        long years = ChronoUnit.YEARS.between(issued, expiry);
        if (years > 4) {
            throw new BusinessRuleValidationException("Invalid license validity period");
        }
    }

    private void validateMedicalDates(LocalDate issued, LocalDate expiry) {
        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleValidationException("Medical issued date cannot be in the future");
        }
        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleValidationException("Medical expiry must be after issued date");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);
        if (months > 6) {
            throw new BusinessRuleValidationException("Medical validity cannot exceed 6 months");
        }
    }

    private void validateUniqueness(DriverCreateRequestDto dto) {
        if (driverRepository.existsByLicensenumber(dto.getLicensenumber())) {
            throw new ValidationException("License number already exists");
        }

        if (driverRepository.existsByNumber(dto.getNumber())) {
            throw new ValidationException("Driver number already exists");
        }
    }

    private void validateUniqueness(DriverUpdateRequestDto dto) {
        // License number uniqueness
        if (driverRepository.existsByLicensenumberAndIdNot(dto.getLicensenumber(), dto.getId())) {
            throw new ResourceExistsException("License number already exists");
        }

        // Driver number uniqueness
        if (driverRepository.existsByNumberAndIdNot(dto.getNumber(), dto.getId())) {
            throw new ResourceExistsException("Driver number already exists");
        }
    }

}
