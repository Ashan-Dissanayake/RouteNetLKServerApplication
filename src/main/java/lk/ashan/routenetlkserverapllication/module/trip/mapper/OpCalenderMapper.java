package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OpCalenderSummaryDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Opcalender;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OpCalenderMapper {
    Opcalender toEntity(OpCalenderSummaryDto opCalenderSummaryDto);

    OpCalenderSummaryDto toDto(Opcalender opCalender);
    List<OpCalenderSummaryDto> toDtoList(List<Opcalender> opCalenders);
}
