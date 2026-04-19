package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring",uses = {
        EmployeeMapper.class
})
public interface RosterAssignmentMapper {

    // Map the nested Designation ID to the flat Fact ID
    @Mapping(target = "designationId", source = "designation.id")
    EmployeeFact toFact(Employee employee);

    // This prevents MapStruct from trying to map individual boolean methods
    @Mapping(target = "id", source = "id")
    @BeanMapping(ignoreByDefault = true) // Only map the ID, ignore helper booleans
    Employee toEntity(EmployeeFact fact);

    @Mapping(target = "id", expression = "java(assignment.getId())")
    @Mapping(target = "rosterShiftId", source = "rostershift.id")
    @Mapping(target = "shiftDate", source = "rostershift.doshift")
    @Mapping(target = "startTime", source = "rostershift.shift.tostart")
    @Mapping(target = "endTime", source = "rostershift.shift.toend")
    @Mapping(target = "designationId", source = "rostershift.designation.id")
    @Mapping(target = "shiftId", source = "rostershift.shift.id")
    @Mapping(target = "requiredFamiliarityLevel", source = "rostershift.requiredFamiliarityLevel")
    RosterShiftAssignmentPlanning toPlanning(RosterShiftAssignment assignment);

//    @Mapping(target = "employee", source = "employeeFact")
//    @Mapping(target = "rostershift", ignore = true)
//    void updateAssignmentFromPlanning(RosterShiftAssignmentPlanning planning, @MappingTarget RosterShiftAssignment entity);


    @Mapping(target = "employeeName", source = "employee.fullname")
    @Mapping(target = "employeeNumber", source = "employee.number")
    @Mapping(target = "designation", source = "employee.designation.name")
    @Mapping(target = "shiftDate", source = "rostershift.doshift")
    @Mapping(target = "shiftName", source = "rostershift.shift.name")
    @Mapping(target = "startTime", source = "rostershift.shift.tostart")
    @Mapping(target = "endTime", source = "rostershift.shift.toend")
    @Mapping(target = "status", source = "rostershiftassignmentstatus.name")
    RosterShiftAssignmentResponseDto toDto(RosterShiftAssignment entity);

    List<RosterShiftAssignmentResponseDto> toDtoList(List<RosterShiftAssignment> entities);
}
