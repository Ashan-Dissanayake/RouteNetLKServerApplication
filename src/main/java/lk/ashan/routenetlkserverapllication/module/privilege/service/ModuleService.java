package lk.ashan.routenetlkserverapllication.module.privilege.service;

import lk.ashan.routenetlkserverapllication.module.privilege.mapper.ModuleMapper;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.ModuleDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.ModuleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @Transactional(readOnly = true)
    public List<ModuleDto> getModules(){
       return moduleMapper.toDtoList(moduleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Module getById(Integer id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module not found"
                ));
    }

}
