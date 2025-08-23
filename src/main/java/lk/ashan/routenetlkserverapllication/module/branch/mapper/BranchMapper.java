package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequest;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchFullResponse;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequest;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
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
