package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchtypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchTypeMapper {

  BranchtypeDto toDto(BranchType branchtype);

  List<BranchtypeDto> toDtoList(List<BranchType> branchTypes);

}
