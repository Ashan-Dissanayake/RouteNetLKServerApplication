package lk.ashan.ntcserverapllication.module.branch.mapper;

import lk.ashan.ntcserverapllication.module.branch.dto.DistrictResponse;
import lk.ashan.ntcserverapllication.module.branch.model.District;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DistrictMapper {

  List<DistrictResponse> toDistrictResponseList(List<District> districts);

}
