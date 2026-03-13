package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchstatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchStatusMapper {

  BranchstatusDto toDto(BranchStatus branchstatus);

  List<BranchstatusDto> toDtoList(List<BranchStatus> branchStatuses);

}
