package lk.ashan.routenetlkserverapllication.module.user.service;

import lk.ashan.routenetlkserverapllication.module.user.mapper.RoleMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.user.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public List<RoleDto> getRoles() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }


    @Transactional(readOnly = true)
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User status '" + name + "' not found"
                ));
    }
}
