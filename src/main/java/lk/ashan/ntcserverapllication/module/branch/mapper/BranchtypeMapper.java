package lk.ashan.ntcserverapllication.module.branch.mapper;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchtypeResponse;
import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchtypeMapper {

  List<BranchtypeResponse> toBranchtypeResponseList(List<Branchtype> branchtypes);

}
