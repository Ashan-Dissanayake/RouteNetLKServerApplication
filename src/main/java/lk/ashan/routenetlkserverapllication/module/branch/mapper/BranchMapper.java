package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchcoverage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
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

  @AfterMapping
  default void linkCoverages(@MappingTarget Branch branch) {
    System.out.println("linkCoverages() called");
  }



}
