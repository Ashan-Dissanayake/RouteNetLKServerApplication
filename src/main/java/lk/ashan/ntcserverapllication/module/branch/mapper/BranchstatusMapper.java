package lk.ashan.ntcserverapllication.module.branch.mapper;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchstatusResponse;
import lk.ashan.ntcserverapllication.module.branch.model.Branchstatus;
import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchstatusMapper {

  List<BranchstatusResponse> toBranchstatusResponseList(List<Branchstatus> branchstatuses);

}
