package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        VehicleMapper.class, BranchMapper.class, PermitStatusMapper.class,
        ServiceTypeMapper.class, RouteMapper.class
})
public interface PermitMapper {
    PermitDetailResponseDto toDto(Permite permite);
    List<PermitDetailResponseDto> toDtoList(List<Permite> permites);

    Permite toEntity(PermitCreateRequestDto request);

    @Mapping(target = "vehicle",source = "vehicle.number")
    PermitSummaryResponseDto toSummaryDto(Permite permite);
    List<PermitSummaryResponseDto> toSummaryDtoList(List<Permite> permites);



}
