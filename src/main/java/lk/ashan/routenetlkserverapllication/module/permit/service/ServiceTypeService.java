package lk.ashan.routenetlkserverapllication.module.permit.service;

import lk.ashan.routenetlkserverapllication.module.permit.mapper.ServiceTypeMapper;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.ServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.ServiceType;
import lk.ashan.routenetlkserverapllication.module.permit.repository.ServiceTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;

    @Transactional(readOnly = true)
    public List<ServiceTypeDto> getServiceTypes(){
       return serviceTypeMapper.toDtoList(serviceTypeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ServiceType getById(Integer id) {
        return serviceTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service type not found"
                ));
    }

}
