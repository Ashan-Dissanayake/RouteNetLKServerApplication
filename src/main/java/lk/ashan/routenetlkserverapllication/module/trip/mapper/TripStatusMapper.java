package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.Triptype;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripStatusMapper {
    Tripstatus toEntity(TripStatusDto tripStatusDto);

    TripStatusDto toDto(Tripstatus tripStatus);
    List<TripStatusDto> toDtoList(List<Tripstatus> tripStatuses);
}
