package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeContextBuilder {

    public EmployeeValidationContext buildForCreate(EmployeeCreateRequestDto dto) {

        return EmployeeValidationContext.builder()
                //.number(dto.getNumber())
                .nic(dto.getNic())
                .mobile(dto.getMobile())
                //.email(dto.getEmail())
                .emergencyContact(dto.getEmergencycontact())

                .departmentName(dto.getDepartment().getName())
                .designationName(dto.getDesignation().getName())
                .genderName(dto.getGender().getName())

                .employeeTypeName(dto.getEmployeetype().getName())
                .dateOfJoining(dto.getDoj())
                .build();
    }

    public EmployeeValidationContext buildForUpdate(EmployeeUpdateRequestDto dto) {

        return EmployeeValidationContext.builder()
                .id(dto.getId())
                //.number(dto.getNumber())
                .nic(dto.getNic())
                .mobile(dto.getMobile())
                //.email(dto.getEmail())
                .emergencyContact(dto.getEmergencycontact())

                .departmentName(dto.getDepartment() != null ? dto.getDepartment().getName() : null)
                .designationName(dto.getDesignation() != null ? dto.getDesignation().getName() : null)
                .genderName(dto.getGender() != null ? dto.getGender().getName() : null)

                .employeeTypeName(dto.getEmployeetype() != null ? dto.getEmployeetype().getName() : null)
                .dateOfJoining(dto.getDoj())
                .build();
    }
}
