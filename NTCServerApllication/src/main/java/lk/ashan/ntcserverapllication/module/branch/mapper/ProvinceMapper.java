package lk.ashan.ntcserverapllication.module.branch.mapper;

import lk.ashan.ntcserverapllication.module.branch.dto.ProvinceResponse;
import lk.ashan.ntcserverapllication.module.branch.model.Province;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProvinceMapper {

  List<ProvinceResponse> toProvinceResponseList(List<Province> provinces);

  ProvinceResponse toProvinceResponse(Province province);

}
