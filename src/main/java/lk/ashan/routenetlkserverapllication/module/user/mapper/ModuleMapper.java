package lk.ashan.routenetlkserverapllication.module.user.mapper;

import lk.ashan.routenetlkserverapllication.module.roster.mapper.RoleMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.ModuleDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        RoleMapper.class, ModuleMapper.class,OperationMapper.class
})
public interface ModuleMapper {
    Module toEntity(ModuleDto moduleDto);
    ModuleDto toDto(Module module);
    List<ModuleDto> toDtoList(List<Module> modules);
}
