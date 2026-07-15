package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserRoleResponseDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRoleMapper {
    UserRole toEntity(UserRoleResponseDto userRoleResponseDto);
    UserRoleResponseDto toDto(UserRoleMapper userRole);
    List<UserRoleResponseDto> toDtoList(List<UserRole> userRoles);
}
