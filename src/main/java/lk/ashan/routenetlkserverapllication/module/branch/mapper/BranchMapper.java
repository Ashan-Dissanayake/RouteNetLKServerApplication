package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        BranchTypeMapper.class,BranchStatusMapper.class,RegionalOfficeMapper.class,
})
public interface BranchMapper {

  BranchDetailResponseDto toDto(Branch branch);
  List<BranchDetailResponseDto> toDtoList(List<Branch> branches);
  List<BranchSummaryResponseDto> toSummaryDetailList(List<Branch> branches);

  Branch toEntity(BranchCreateRequestDto request);
  Branch toEntity(BranchUpdateRequestDto request);

  // This method updates an existing entity with values from DTO
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "branchstatus", ignore = true) // handled manually
  void updateEntityFromDto(BranchUpdateRequestDto dto, @MappingTarget Branch entity);
}
