package lk.ashan.routenetlkserverapllication.module.permit.service;

import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitStatusDto;
import lk.ashan.routenetlkserverapllication.module.permit.mapper.PermitStatusMapper;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermitStatusService {

    private final PermitStatusRepository permitStatusRepository;
    private final PermitStatusMapper permitStatusMapper;

    public List<PermitStatusDto> getPermitStatuses(){
       return permitStatusMapper.toDtoList(permitStatusRepository.findAll());
    }

}
