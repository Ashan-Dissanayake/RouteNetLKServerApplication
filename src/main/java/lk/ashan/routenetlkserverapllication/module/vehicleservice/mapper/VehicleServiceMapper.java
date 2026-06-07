package lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceExecution;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {VehicleServiceStatusMapper.class, BranchMapper.class, VehicleServicePriorityMapper.class,
                VehicleServiceTypeMapper.class, VehicleMapper.class, IncidentMapper.class,VehicleServicePartMapper.class

        }
)
public interface VehicleServiceMapper {
    VehicleServiceSummaryDto toSummaryDto(VehicleService entity);

    VehicleServiceDetailResponseDto toDto(VehicleService entity);
    List<VehicleServiceDetailResponseDto> toDtoList(List<VehicleService> entities);

    VehicleService toEntity (VehicleServiceCreateRequestDto dto);

    @Mapping(target = "startodometer", source = "startodometer")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "vehicleservice", ignore = true)
    @Mapping(target = "maintechnician", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "dostarted", ignore = true)
    @Mapping(target = "doend", ignore = true)
    @Mapping(target = "remarks", ignore = true)
    @Mapping(target = "nextserviceinkm", ignore = true)
    VehicleServiceExecution toExecutionEntity(VehicleServiceStartRequestDto dto);


    @Mapping(target = "remarks", source = "remarks")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "vehicleservice", ignore = true)
    @Mapping(target = "dostarted", ignore = true)
    @Mapping(target = "doend", ignore = true)
    @Mapping(target = "startodometer", ignore = true)
    @Mapping(target = "nextserviceinkm", ignore = true)
    @Mapping(target = "maintechnician", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateExecutionWithCompletePayload(
            VehicleServiceCompleteRequestDto dto,
            @MappingTarget VehicleServiceExecution execution
    );
}
