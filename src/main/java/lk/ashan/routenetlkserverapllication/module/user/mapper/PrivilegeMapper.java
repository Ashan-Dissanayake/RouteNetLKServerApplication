package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.PrivilegeDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrivilegeMapper {
    Privilege toEntity(PrivilegeDto privilegeDto);
    PrivilegeDto toDto(Privilege privilege);
    List<PrivilegeDto> toDtoList(List<Privilege> privileges);
}
