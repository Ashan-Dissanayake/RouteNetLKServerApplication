package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
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

  Branch toEntity(BranchCreateRequestDto request);

  Branch toEntity(BranchUpdateRequestDto request);

  List<BranchDetailResponseDto> toDetailList(List<Branch> branches);

  // This method updates an existing entity with values from DTO
  @Mapping(target = "id", ignore = true) // usually you don’t want to override the id
  void updateEntityFromDto(BranchUpdateRequestDto dto, @MappingTarget Branch entity);
}
