package lk.ashan.ntcserverapllication.module.branch.mapper;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchCreateRequest;
import lk.ashan.ntcserverapllication.module.branch.dto.BranchFullResponse;
import lk.ashan.ntcserverapllication.module.branch.dto.BranchUpdateRequest;
import lk.ashan.ntcserverapllication.module.branch.model.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {

  List<BranchFullResponse> toFulBranchResponseList(List<Branch> branches);

  BranchFullResponse toFullBranchResponse(Branch branch);

  Branch toBranchEntity(BranchCreateRequest request);

  Branch toBranchEntity(BranchUpdateRequest request);

}
