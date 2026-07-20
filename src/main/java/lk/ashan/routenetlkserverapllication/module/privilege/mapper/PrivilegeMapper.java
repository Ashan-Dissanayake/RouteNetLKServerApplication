package lk.ashan.routenetlkserverapllication.module.privilege.mapper;

import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeResponseDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrivilegeMapper {
    Privilege toEntity(PrivilegeResponseDto privilegeResponseDto);
    PrivilegeResponseDto toDto(Privilege privilege);
    List<PrivilegeResponseDto> toDtoList(List<Privilege> privileges);
}
