package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchStatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchStatusMapper {

  BranchStatusDto toDto(BranchStatus branchstatus);

  List<BranchStatusDto> toDtoList(List<BranchStatus> branchStatuses);

}
