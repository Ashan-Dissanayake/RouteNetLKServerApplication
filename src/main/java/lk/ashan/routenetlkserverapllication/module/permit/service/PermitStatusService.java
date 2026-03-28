package lk.ashan.routenetlkserverapllication.module.permit.service;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitStatusDto;
import lk.ashan.routenetlkserverapllication.module.permit.mapper.PermitStatusMapper;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermitStatusService {

    private final PermitStatusRepository permitStatusRepository;
    private final PermitStatusMapper permitStatusMapper;

    @Transactional(readOnly = true)
    public List<PermitStatusDto> getPermitStatuses(){
       return permitStatusMapper.toDtoList(permitStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PermiteStatus getById(Integer id) {
        return permitStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
