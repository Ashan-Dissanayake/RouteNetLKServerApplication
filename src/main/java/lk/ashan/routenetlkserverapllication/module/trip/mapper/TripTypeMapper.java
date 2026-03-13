package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Triptype;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripTypeMapper {
    Triptype toEntity(TripTypeDto triptypeDto);

    TripTypeDto toDto(Triptype tripType);
    List<TripTypeDto> toDtoList(List<Triptype> tripTypes);
}
