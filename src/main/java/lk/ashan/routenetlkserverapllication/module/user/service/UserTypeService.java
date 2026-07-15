package lk.ashan.routenetlkserverapllication.module.user.service;

import lk.ashan.routenetlkserverapllication.module.user.mapper.UserTypeMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserTypeDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserType;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;
    private final UserTypeMapper userTypeMapper;

    @Transactional(readOnly = true)
    public List<UserTypeDto> getUserTypes() {
        return userTypeMapper.toDtoList(userTypeRepository.findAll());
    }


    @Transactional(readOnly = true)
    public UserType getByName(String name) {
        return userTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User type '" + name + "' not found"
                ));
    }


}
