package lk.ashan.routenetlkserverapllication.module.sparepart.service;

import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMasterMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartMasterDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partmaster;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartMasterRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartMasterService {

    private final PartMasterRepository partMasterRepository;
    private final PartMasterMapper partMasterMapper;

    @Transactional(readOnly = true)
    public List<PartMasterDto> getPartMasters(){
       return partMasterMapper.toDtoList(partMasterRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Partmaster getById(Integer id) {
        return partMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    @Transactional(readOnly = true)
    public Partmaster getByName(String name) {
        return partMasterRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
