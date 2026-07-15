package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalOfficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting between `RegionalOffice` entities and `RegionalOfficeDto` objects.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegionalOfficeMapper {

  /**
   * Converts a `RegionalOffice` entity to a `RegionalOfficeDto`.
   *
   * @param regionaloffice the `RegionalOffice` entity to be converted
   * @return the corresponding `RegionalOfficeDto`
   */
  RegionalOfficeDto toDto(RegionalOffice regionaloffice);

  /**
   * Converts a list of `RegionalOffice` entities to a list of `RegionalOfficeDto` objects.
   *
   * @param regionalOffices the list of `RegionalOffice` entities to be converted
   * @return the corresponding list of `RegionalOfficeDto` objects
   */
  List<RegionalOfficeDto> toDtoList(List<RegionalOffice> regionalOffices);

}
