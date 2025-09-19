package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {

  List<BranchDetailResponseDto> toFulBranchResponseList(List<Branch> branches);

  BranchDetailResponseDto toFullBranchResponse(Branch branch);

  Branch toBranchEntity(BranchCreateRequestDto request);

  Branch toBranchEntity(BranchUpdateRequestDto request);

}
