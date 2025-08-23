package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchstatusResponse;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchstatusMapper {

  List<BranchstatusResponse> toBranchstatusResponseList(List<Branchstatus> branchstatuses);

}
