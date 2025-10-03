package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDistrictCoverageDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchcoverage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { DistrictMapper.class })
public interface BranchCoverageMapper {
    Branchcoverage toEntity(BranchDistrictCoverageDto dto);

}
