package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Triptype;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between TripTypeDto and Triptype entities.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripTypeMapper {

    /**
     * Converts a TripTypeDto object to a Triptype entity.
     *
     * @param triptypeDto the TripTypeDto object to be converted
     * @return the corresponding Triptype entity
     */
    Triptype toEntity(TripTypeDto triptypeDto);

    /**
     * Converts a Triptype entity to a TripTypeDto object.
     *
     * @param tripType the Triptype entity to be converted
     * @return the corresponding TripTypeDto object
     */
    TripTypeDto toDto(Triptype tripType);

    /**
     * Converts a list of Triptype entities to a list of TripTypeDto objects.
     *
     * @param tripTypes the list of Triptype entities to be converted
     * @return the corresponding list of TripTypeDto objects
     */
    List<TripTypeDto> toDtoList(List<Triptype> tripTypes);
}
