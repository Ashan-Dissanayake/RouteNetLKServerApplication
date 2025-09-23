package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDistrictCoverageDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchcoverage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { DistrictMapper.class })
public interface BranchCoverageMapper {
    Branchcoverage toEntity(BranchDistrictCoverageDto dto);
    List<Branchcoverage> toEntityList(List<BranchDistrictCoverageDto> dtos);
}
