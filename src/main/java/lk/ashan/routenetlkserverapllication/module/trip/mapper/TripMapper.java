package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.mapper.PermitMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting between `Trip` entities and their corresponding DTOs.
 * Utilizes other mappers such as `TripTypeMapper`, `TripStatusMapper`, `OriginTerminalMapper`,
 * `PermitMapper`, and `OpCalenderMapper` for nested mappings.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        TripTypeMapper.class, TripStatusMapper.class, OriginTerminalMapper.class,
        PermitMapper.class, OpCalenderMapper.class
})
public interface TripMapper {

    /**
     * Converts a `Trip` entity to a `TripDetailResponseDto`.
     *
     * @param trip the `Trip` entity to convert
     * @return the corresponding `TripDetailResponseDto`
     */
    TripDetailResponseDto toDto(Trip trip);

    /**
     * Converts a list of `Trip` entities to a list of `TripDetailResponseDto`.
     *
     * @param trips the list of `Trip` entities to convert
     * @return the corresponding list of `TripDetailResponseDto`
     */
    List<TripDetailResponseDto> toDetailList(List<Trip> trips);

    /**
     * Converts a `Trip` entity to a `TripSummaryResponseDto`.
     *
     * @param trip the `Trip` entity to convert
     * @return the corresponding `TripSummaryResponseDto`
     */
    TripSummaryResponseDto toSummaryDto(Trip trip);

    /**
     * Converts a list of `Trip` entities to a list of `TripSummaryResponseDto`.
     *
     * @param trip the list of `Trip` entities to convert
     * @return the corresponding list of `TripSummaryResponseDto`
     */
    List<TripSummaryResponseDto> toDto(List<Trip> trip);

    /**
     * Converts a `TripCreateRequestDto` to a `Trip` entity.
     *
     * @param requestDto the `TripCreateRequestDto` to convert
     * @return the corresponding `Trip` entity
     */
    Trip toEntity(TripCreateRequestDto requestDto);

    /**
     * Converts a `TripUpdateRequestDto` to a `Trip` entity.
     *
     * @param requestDto the `TripUpdateRequestDto` to convert
     * @return the corresponding `Trip` entity
     */
    Trip toEntity(TripUpdateRequestDto requestDto);

}
