package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.RegionalofficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Regionaloffice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegionalofficeMapper {

  RegionalofficeDto toDto(Regionaloffice regionaloffice);

  List<RegionalofficeDto> toDtoList(List<Regionaloffice> regionaloffices);

}
