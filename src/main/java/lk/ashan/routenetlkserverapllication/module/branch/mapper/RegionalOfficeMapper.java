package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalOfficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegionalOfficeMapper {

  RegionalOfficeDto toDto(RegionalOffice regionaloffice);

  List<RegionalOfficeDto> toDtoList(List<RegionalOffice> regionalOffices);

}
