package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchStatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting between BranchStatus entities and DTOs.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchStatusMapper {

  /**
   * Converts a BranchStatus entity to a BranchStatusDto.
   *
   * @param branchstatus the BranchStatus entity to convert
   * @return the converted BranchStatusDto
   */
  BranchStatusDto toDto(BranchStatus branchstatus);

  /**
   * Converts a list of BranchStatus entities to a list of BranchStatusDto objects.
   *
   * @param branchStatuses the list of BranchStatus entities to convert
   * @return the list of converted BranchStatusDto objects
   */
  List<BranchStatusDto> toDtoList(List<BranchStatus> branchStatuses);

}
