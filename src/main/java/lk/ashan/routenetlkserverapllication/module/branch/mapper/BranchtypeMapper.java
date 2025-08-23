package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchtypeResponse;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchtype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchtypeMapper {

  List<BranchtypeResponse> toBranchtypeResponseList(List<Branchtype> branchtypes);

}
