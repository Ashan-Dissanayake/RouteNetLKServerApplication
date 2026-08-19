package lk.ashan.routenetlkserverapllication.module.farecollection.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMasterMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartStatusMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.tripexecution.mapper.TripExecutionMapper;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between `FareCollection` entities and DTOs.
 * Utilizes other mappers such as `BranchMapper`, `TripExecutionMapper`, and `TicketMachineMapper`.
 * Configured with Spring's component model and ignores unmapped target properties.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
       BranchMapper.class, TripExecutionMapper.class, TicketMachineMapper.class
})
public interface FareCollectionMapper {

    /**
     * Converts a `FareCollection` entity to a `FareCollectionDetailResponseDto`.
     *
     * @param fareCollection the `FareCollection` entity to convert
     * @return the converted `FareCollectionDetailResponseDto`
     */
    FareCollectionDetailResponseDto toDto(FareCollection fareCollection);

    /**
     * Converts a list of `FareCollection` entities to a list of `FareCollectionDetailResponseDto`s.
     *
     * @param fareCollections the list of `FareCollection` entities to convert
     * @return the list of converted `FareCollectionDetailResponseDto`s
     */
    List<FareCollectionDetailResponseDto> toDtoList(List<FareCollection> fareCollections);

    /**
     * Converts a `FareCollectionCreateRequestDto` to a `FareCollection` entity.
     *
     * @param createRequestDto the `FareCollectionCreateRequestDto` to convert
     * @return the converted `FareCollection` entity
     */
    FareCollection toEntity(FareCollectionCreateRequestDto createRequestDto);
}
