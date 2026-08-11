package lk.ashan.routenetlkserverapllication.module.crew.mapper;


import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting between `Driver` entities and DTOs.
 * Utilizes other mappers such as `EmployeeMapper`, `LicenseCategoryMapper`,
 * `RouteFamiliarityLevelMapper`, and `CrewStatusMapper`.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        EmployeeMapper.class, LicenseCategoryMapper.class,
        RouteFamiliarityLevelMapper.class, CrewStatusMapper.class
})
public interface DriverMapper {

    /**
     * Converts a list of `Driver` entities to a list of `DriverDetailResponseDto`.
     *
     * @param drivers the list of `Driver` entities to convert
     * @return a list of `DriverDetailResponseDto`
     */
    List<DriverDetailResponseDto> toDtoList(List<Driver> drivers);

    /**
     * Converts a `Driver` entity to a `DriverDetailResponseDto`.
     *
     * @param driver the `Driver` entity to convert
     * @return the converted `DriverDetailResponseDto`
     */
    DriverDetailResponseDto toDto(Driver driver);

    /**
     * Converts a `DriverCreateRequestDto` to a `Driver` entity.
     *
     * @param driverCreateRequestDto the DTO containing data for creating a `Driver`
     * @return the created `Driver` entity
     */
    Driver toEntity(DriverCreateRequestDto driverCreateRequestDto);

    /**
     * Converts a `DriverUpdateRequestDto` to a `Driver` entity.
     *
     * @param driverUpdateRequestDto the DTO containing data for updating a `Driver`
     * @return the updated `Driver` entity
     */
    Driver toEntity(DriverUpdateRequestDto driverUpdateRequestDto);

    /**
     * Updates an existing `Driver` entity with data from a `DriverUpdateRequestDto`.
     * Certain fields are ignored during the update process.
     *
     * @param dto the DTO containing updated data for the `Driver`
     * @param entity the existing `Driver` entity to update
     * @return the updated `Driver` entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "licensenumber", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "dolicenseissued", ignore = true)
    @Mapping(target = "licensecategory", ignore = true)
    @Mapping(target = "routefamiliaritylevel", ignore = true)
    @Mapping(target = "crewstatus", ignore = true)
    Driver updateEntityFromDto(DriverUpdateRequestDto dto, @MappingTarget Driver entity);

}
