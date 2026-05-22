package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
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

  @Mapping(target = "regionalOfficeId", source = "regionaloffice.id")
  BranchSummaryDto toSummaryDto(Branch branch);
  List<BranchSummaryDto> toSummaryDtolList(List<Branch> branches);

  Branch toEntity(BranchCreateRequestDto request);
  //Branch toEntity(BranchUpdateRequestDto request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "branchstatus", ignore = true)
  @Mapping(target = "branchtype", ignore = true)
  @Mapping(target = "regionaloffice", ignore = true)
  Branch updateEntityFromDto(BranchUpdateRequestDto dto, @MappingTarget Branch entity);
}
