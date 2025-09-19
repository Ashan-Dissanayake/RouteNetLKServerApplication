package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchstatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchstatusMapper {

  List<BranchstatusDto> toBranchstatusResponseList(List<Branchstatus> branchstatuses);

}
