package lk.ashan.routenetlkserverapllication.module.privilege.mapper;

import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.ModuleDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import lk.ashan.routenetlkserverapllication.module.user.mapper.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        RoleMapper.class,
        ModuleMapper.class,OperationMapper.class
})
public interface ModuleMapper {
    Module toEntity(ModuleDto moduleDto);
    ModuleDto toDto(Module module);
    List<ModuleDto> toDtoList(List<Module> modules);
}
