package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        BranchtypeMapper.class,BranchstatusMapper.class,ProvinceMapper.class,
        DistrictMapper.class,BranchCoverageMapper.class
})
public interface BranchMapper {

  BranchDetailResponseDto toDto(Branch branch);
  List<BranchDetailResponseDto> toDetailList(List<Branch> branches);

  List<BranchSummaryResponseDto> toSummaryDetailList(List<Branch> branches);

  Branch toEntity(BranchCreateRequestDto request);

  Branch toEntity(BranchUpdateRequestDto request);


  // This method updates an existing entity with values from DTO
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "branchstatus", ignore = true) // handled manually
  void updateEntityFromDto(BranchUpdateRequestDto dto, @MappingTarget Branch entity);
}
