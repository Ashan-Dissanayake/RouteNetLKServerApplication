package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserStatusDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserStatusMapper {
    UserStatus toEntity(UserStatusDto userStatusDto);
    UserStatusDto toDto(UserStatus userStatus);
    List<UserStatusDto> toDtoList(List<UserStatus> userStatuses);
}
