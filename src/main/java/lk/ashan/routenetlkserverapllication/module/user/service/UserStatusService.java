package lk.ashan.routenetlkserverapllication.module.user.service;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.user.mapper.UserStatusMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserStatusDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserStatus;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional(readOnly = true)
    public List<UserStatusDto> getUserStatuses() {
        return userStatusMapper.toDtoList(userStatusRepository.findAll());
    }


    @Transactional(readOnly = true)
    public UserStatus getByName(String name) {
        return userStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User status '" + name + "' not found"
                ));
    }


}
