package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripStatusMapper {
    Tripstatus toEntity(TripStatusDto tripstatusDto);

    TripStatusDto toDto(Tripstatus tripstatus);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tripstatus partialUpdate(TripStatusDto tripstatusDto, @MappingTarget Tripstatus tripstatus);
}
