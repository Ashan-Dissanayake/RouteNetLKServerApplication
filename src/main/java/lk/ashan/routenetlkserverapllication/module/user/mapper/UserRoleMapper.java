package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserRoleDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRoleMapper {
    UserRole toEntity(UserRoleDto userRoleDto);
    UserRoleDto toDto(UserRoleMapper userRole);
    List<UserRoleDto> toDtoList(List<UserRoleMapper> userRoles);
}
