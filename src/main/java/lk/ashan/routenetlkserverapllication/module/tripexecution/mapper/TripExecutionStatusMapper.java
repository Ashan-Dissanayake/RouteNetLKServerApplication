package lk.ashan.routenetlkserverapllication.module.tripexecution.mapper;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionStatusDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripExecutionStatusMapper {
    TripExecutionStatusDto toDto(TripExecutionStatus tripExecutionStatus);
    List<TripExecutionStatusDto> toDtoList(List<TripExecutionStatus> tripExecutionStatuses);
}
