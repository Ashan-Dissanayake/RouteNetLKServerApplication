package lk.ashan.routenetlkserverapllication.module.sparepart.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartMasterDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partmaster;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartMasterMapper {
    PartMasterDto toDto(Partmaster partMaster);
    List<PartMasterDto> toDtoList(List<Partmaster> partMasters);
}
