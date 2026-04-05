package lk.ashan.routenetlkserverapllication.module.partreqest.service;

import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestStatusMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestStatusDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartRequestStatusService {

    private final PartRequestStatusRepository partRequestStatusRepository;
    private final PartRequestStatusMapper partRequestStatusMapper;

    @Transactional(readOnly = true)
    public List<PartRequestStatusDto> getPartRequestStatuses(){
       return partRequestStatusMapper.toDtoList(partRequestStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PartRequestStatus getById(Integer id) {
        return partRequestStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    @Transactional(readOnly = true)
    public PartRequestStatus getByName(String name) {
        return partRequestStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
