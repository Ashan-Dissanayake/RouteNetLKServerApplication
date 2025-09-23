package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchtypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchtype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchtypeMapper {

  BranchtypeDto toDto(Branchtype branchtype);

  List<BranchtypeDto> toDtoList(List<Branchtype> branchtypes);

}
