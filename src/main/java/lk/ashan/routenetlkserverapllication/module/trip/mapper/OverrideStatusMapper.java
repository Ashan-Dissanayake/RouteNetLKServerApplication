package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.dto.OverrideStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Overridestatus;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OverrideStatusMapper {
    Overridestatus toEntity(OverrideStatusDto overridestatusDto);

    OverrideStatusDto toDto(Overridestatus overridestatus);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Overridestatus partialUpdate(OverrideStatusDto overridestatusDto, @MappingTarget Overridestatus overridestatus);
}
