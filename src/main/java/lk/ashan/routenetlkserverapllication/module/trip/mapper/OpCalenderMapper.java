package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OpCalenderSummaryDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Opcalender;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting between `Opcalender` entities and `OpCalenderSummaryDto` objects.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OpCalenderMapper {

    /**
     * Converts an `OpCalenderSummaryDto` object to an `Opcalender` entity.
     *
     * @param opCalenderSummaryDto the DTO to be converted
     * @return the corresponding `Opcalender` entity
     */
    Opcalender toEntity(OpCalenderSummaryDto opCalenderSummaryDto);

    /**
     * Converts an `Opcalender` entity to an `OpCalenderSummaryDto` object.
     *
     * @param opCalender the entity to be converted
     * @return the corresponding `OpCalenderSummaryDto` object
     */
    OpCalenderSummaryDto toDto(Opcalender opCalender);

    /**
     * Converts a list of `Opcalender` entities to a list of `OpCalenderSummaryDto` objects.
     *
     * @param opCalenders the list of entities to be converted
     * @return the corresponding list of `OpCalenderSummaryDto` objects
     */
    List<OpCalenderSummaryDto> toDtoList(List<Opcalender> opCalenders);
}
