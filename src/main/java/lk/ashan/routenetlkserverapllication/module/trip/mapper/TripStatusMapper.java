package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripStatusMapper {
    Tripstatus toEntity(TripStatusDto tripStatusDto);

    TripStatusDto toDto(Tripstatus tripStatus);
    List<TripStatusDto> toDtoList(List<Tripstatus> tripStatuses);
}
