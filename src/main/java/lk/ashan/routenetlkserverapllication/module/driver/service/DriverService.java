package lk.ashan.routenetlkserverapllication.module.driver.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.driver.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.driver.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.driver.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.driver.mapper.DriverMapper;
import lk.ashan.routenetlkserverapllication.module.driver.model.Driver;
import lk.ashan.routenetlkserverapllication.module.driver.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleValidationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
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

    public DriverDetailResponseDto createDriver(@Valid @NotNull DriverCreateRequestDto createRequestDto){

        validateUniqueness(createRequestDto);
        validateLicenseDateRangeValidity(createRequestDto.getDolicenseissued(),createRequestDto.getDolicenseexpired());
        validateMedicalDateRangeValidity(createRequestDto.getDolicenseissued(),createRequestDto.getDolicenseexpired());

        if (!createRequestDto.getCrewstatus().getName().equalsIgnoreCase("Eligible")) {
            throw new InvalidStatusException("New driver must have status 'ELIGIBLE'");
        }

        Driver driver =  driverMapper.toEntity(createRequestDto);
        Driver savedDriver = driverRepository.save(driver);

        return driverMapper.toDto(savedDriver);
    }

    private void validateLicenseDateRangeValidity(LocalDate issuedDate, LocalDate expiryDate){

        final int MAX_ALLOWED_YEARS = 8;

        long years = ChronoUnit.YEARS.between(issuedDate, expiryDate);

        if (years>MAX_ALLOWED_YEARS) throw new BusinessRuleValidationException("Invalid license validity period");

    }

    private void validateMedicalDateRangeValidity(LocalDate issuedDate, LocalDate expiryDate) {

        final int MAX_ALLOWED_MONTHS = 6;

        long months = ChronoUnit.MONTHS.between(issuedDate, expiryDate);
        if (months > MAX_ALLOWED_MONTHS) {
            throw new BusinessRuleValidationException(
                    "Medical validity cannot exceed 6 months"
            );
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


}
