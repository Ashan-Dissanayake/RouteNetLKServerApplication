package lk.ashan.routenetlkserverapllication.module.grn.service;

import lk.ashan.routenetlkserverapllication.module.grn.mapper.GrnStatusMapper;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnStatusDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrnStatusService {

    private final GrnStatusRepository grnStatusRepository;
    private final GrnStatusMapper grnStatusMapper;

    @Transactional(readOnly = true)
    public List<GrnStatusDto> getGrnStatuses(){
       return grnStatusMapper.toDtoList(grnStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public GrnStatus getById(Integer id) {
        return grnStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    @Transactional(readOnly = true)
    public GrnStatus getByName(String name) {
        return grnStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
