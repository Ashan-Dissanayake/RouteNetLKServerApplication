package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between TripStatusDto and Tripstatus entities.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripStatusMapper {

    /**
     * Converts a TripStatusDto object to a Tripstatus entity.
     *
     * @param tripStatusDto the DTO to be converted
     * @return the corresponding Tripstatus entity
     */
    Tripstatus toEntity(TripStatusDto tripStatusDto);

    /**
     * Converts a Tripstatus entity to a TripStatusDto object.
     *
     * @param tripStatus the entity to be converted
     * @return the corresponding TripStatusDto object
     */
    TripStatusDto toDto(Tripstatus tripStatus);

    /**
     * Converts a list of Tripstatus entities to a list of TripStatusDto objects.
     *
     * @param tripStatuses the list of entities to be converted
     * @return the corresponding list of TripStatusDto objects
     */
    List<TripStatusDto> toDtoList(List<Tripstatus> tripStatuses);
}
