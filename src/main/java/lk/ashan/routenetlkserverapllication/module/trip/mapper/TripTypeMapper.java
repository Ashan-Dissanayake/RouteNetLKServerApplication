package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Triptype;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripTypeMapper {
    Triptype toEntity(TripTypeDto triptypeDto);

    TripTypeDto toDto(Triptype triptype);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Triptype partialUpdate(TripTypeDto triptypeDto, @MappingTarget Triptype triptype);
}
