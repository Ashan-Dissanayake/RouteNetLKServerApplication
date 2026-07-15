package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserSummaryDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        UserStatusMapper.class, UserTypeMapper.class, UserRoleMapper.class, EmployeeMapper.class
})
public interface UserMapper {
    User toEntity(UserCreateRequestDto userCreateRequestDto);
    User toEntity(UserUpdateRequestDto userUpdateRequestDto);
    UserDetailResponseDto toDto(User user);
    List<UserDetailResponseDto> toDtoList(List<User> users);
    UserSummaryDto toSummaryDto(User user);
}
