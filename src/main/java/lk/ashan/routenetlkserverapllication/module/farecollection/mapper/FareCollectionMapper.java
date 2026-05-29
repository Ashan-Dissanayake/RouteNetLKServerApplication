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

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
       BranchMapper.class, TripExecutionMapper.class, TicketMachineMapper.class
})
public interface FareCollectionMapper {
    FareCollectionDetailResponseDto toDto(FareCollection fareCollection);
    List<FareCollectionDetailResponseDto> toDtoList(List<FareCollection> fareCollections);

    FareCollection toEntity(FareCollectionCreateRequestDto createRequestDto);
}
