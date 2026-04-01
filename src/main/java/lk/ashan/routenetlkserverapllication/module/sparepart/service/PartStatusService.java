package lk.ashan.routenetlkserverapllication.module.sparepart.service;

import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartStatusMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartStatusService {

    private final PartStatusRepository partStatusRepository;
    private final PartStatusMapper partStatusMapper;

    @Transactional(readOnly = true)
    public List<PartStatusDto> getPartStatuses(){
       return partStatusMapper.toDtoList(partStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Partstatus getById(Integer id) {
        return partStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    @Transactional(readOnly = true)
    public Partstatus getByName(String name) {
        return partStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
