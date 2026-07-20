package lk.ashan.routenetlkserverapllication.module.privilege.service;

import lk.ashan.routenetlkserverapllication.module.privilege.mapper.OperationMapper;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.OperationDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.OperationRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    @Transactional(readOnly = true)
    public List<OperationDto> getOperations(){
       return operationMapper.toDtoList(operationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Operation getById(Integer id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operation not found"
                ));
    }

}
