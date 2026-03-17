package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserTypeDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserTypeMapper {
    UserType toEntity(UserTypeDto userTypeDto);
    UserTypeDto toDto(UserType userType);
    List<UserTypeDto> toDtoList(List<UserType> userTypes);
}
