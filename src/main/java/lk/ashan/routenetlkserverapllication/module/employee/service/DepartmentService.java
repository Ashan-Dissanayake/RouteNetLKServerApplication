package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DepartmentDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.DepartmentMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Department;
import lk.ashan.routenetlkserverapllication.module.employee.repository.DepartmentRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    public List<DepartmentDto> getDepartments(){
       return departmentMapper.toDtoList(departmentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Department getById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gender not found"
                ));
    }


}
