package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.OperationDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ModuleMapper.class
})
public interface OperationMapper {
    Operation toEntity(OperationDto operationDto);
    OperationDto toDto(Operation operation);
    List<OperationDto> toDtoList(List<Operation> operations);
}
