package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchTypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting between BranchType entities and BranchTypeDto objects.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchTypeMapper {

  /**
   * Converts a BranchType entity to a BranchTypeDto.
   *
   * @param branchtype the BranchType entity to be converted
   * @return the corresponding BranchTypeDto
   */
  BranchTypeDto toDto(BranchType branchtype);

  /**
   * Converts a list of BranchType entities to a list of BranchTypeDto objects.
   *
   * @param branchTypes the list of BranchType entities to be converted
   * @return the corresponding list of BranchTypeDto objects
   */
  List<BranchTypeDto> toDtoList(List<BranchType> branchTypes);

}
