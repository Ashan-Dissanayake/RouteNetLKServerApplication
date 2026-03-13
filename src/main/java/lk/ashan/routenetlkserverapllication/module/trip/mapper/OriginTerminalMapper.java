package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OriginTerminalDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OriginTerminalMapper {
    Originterminal toEntity(OriginTerminalDto originTerminalDto);

    OriginTerminalDto toDto(Originterminal originTerminal);
    List<OriginTerminalDto> toDtoList(List<Originterminal> originTerminals);
}
