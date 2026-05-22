package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.projection.EmployeeFamiliarityProjection;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface RosterAssignmentMapper {

    // 1. Map Projection to Fact (For Solver)
    EmployeeFact toFact(EmployeeFamiliarityProjection projection);

    // 2. Map Entity to Fact
    @Mapping(target = "designationId", source = "designation.id")
    @Mapping(target = "familiarityLevel", expression = "java(getFamiliarityLevel(employee))")
    EmployeeFact toFact(Employee employee);

    @Mapping(target = "id", source = "id")
    @BeanMapping(ignoreByDefault = true)
    Employee toEntity(EmployeeFact fact);

    // 3. Map Assignment to Planning (For Solver)
    @Mapping(target = "id", expression = "java(assignment.getId())")
    @Mapping(target = "rosterShiftId", source = "rostershift.id")
    @Mapping(target = "shiftDate", source = "rostershift.doshift")
    @Mapping(target = "startTime", source = "rostershift.shift.tostart")
    @Mapping(target = "endTime", source = "rostershift.shift.toend")
    @Mapping(target = "designationId", source = "rostershift.designation.id")
    @Mapping(target = "shiftId", source = "rostershift.shift.id")
    @Mapping(target = "requiredFamiliarityLevel", source = "rostershift.requiredFamiliarityLevel")
    RosterShiftAssignmentPlanning toPlanning(RosterShiftAssignment assignment);

    // 4. Standard DTO Mappings
    @Mapping(target = "employeeName", source = "employee.fullname")
    @Mapping(target = "employeeNumber", source = "employee.number")
    @Mapping(target = "designation", source = "employee.designation.name")
    @Mapping(target = "shiftDate", source = "rostershift.doshift")
    @Mapping(target = "shiftName", source = "rostershift.shift.name")
    @Mapping(target = "startTime", source = "rostershift.shift.tostart")
    @Mapping(target = "endTime", source = "rostershift.shift.toend")
    @Mapping(target = "status", source = "rostershiftassignmentstatus.name")
    @Mapping(target = "rosterName", expression = "java(entity.getRostershift().getRoster().getDostartofweek() + \" to \" + entity.getRostershift().getRoster().getDoendofweek())")
    RosterShiftAssignmentResponseDto toDto(RosterShiftAssignment entity);

    List<RosterShiftAssignmentResponseDto> toDtoList(List<RosterShiftAssignment> entities);



    default Integer getFamiliarityLevel(Employee employee) {
        if (employee == null) return 1;
        if (employee.getDriver() != null && employee.getDriver().getRoutefamiliaritylevel() != null) {
            return employee.getDriver().getRoutefamiliaritylevel().getId();
        } else if (employee.getConductor() != null && employee.getConductor().getRoutefamiliaritylevel() != null) {
            return employee.getConductor().getRoutefamiliaritylevel().getId();
        }
        return 1;
    }
}
