package lk.ashan.routenetlkserverapllication.module.grn.mapper;

import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnPartDto;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnStatusDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grnpart;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grnstatus;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {PartMapper.class})
public interface GrnPartMapper {
    GrnPartDto toDto(Grnpart grnPart);
    List<GrnPartDto> toDtoList(List<Grnpart> grnParts);
}
