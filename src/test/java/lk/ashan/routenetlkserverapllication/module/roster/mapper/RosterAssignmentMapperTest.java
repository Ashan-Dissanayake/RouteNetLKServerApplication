package lk.ashan.routenetlkserverapllication.module.roster.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RosterAssignmentMapperTest {

    private final RosterAssignmentMapper mapper = Mappers.getMapper(RosterAssignmentMapper.class);

    @Test
    void toPlanning_ShouldMapNestedEntityToFlattenedModel() {
        // 1. Setup JPA Entities (Mocking the DB structure)
        Designation driverType = new Designation();
        driverType.setId(1);
        driverType.setName("Driver");

        Shift baseShift = new Shift();
        baseShift.setId(50);
        baseShift.setTostart(LocalTime.of(8, 0));
        baseShift.setToend(LocalTime.of(16, 0));

        RosterShift rosterShift = new RosterShift();
        rosterShift.setId(100);
        rosterShift.setDoshift(LocalDate.of(2026, 4, 20));
        rosterShift.setShift(baseShift);
        rosterShift.setDesignation(driverType);

        RosterShiftAssignment assignment = new RosterShiftAssignment();
        assignment.setId(500); // This is the ID we need to save back later
        assignment.setRostershift(rosterShift);

        // 2. Execute Mapping
        RosterShiftAssignmentPlanning planning = mapper.toPlanning(assignment);

        // 3. Verify (AssertJ)
        assertThat(planning).isNotNull();
        assertThat(planning.getId()).isEqualTo(500); // Primary Key preserved
        assertThat(planning.getRosterShiftId()).isEqualTo(100); // Link to roster preserved
        assertThat(planning.getStartTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(planning.getDesignationId()).isEqualTo(1);
    }

    @Test
    void toFact_ShouldMapEmployeeToFact() {

        Designation driverType = new Designation();
        driverType.setId(1);
        driverType.setName("Driver");


        Employee emp = new Employee();
        emp.setId(10);
        emp.setFullname("Saman Kumara");
        emp.setDesignation(driverType);

        EmployeeFact fact = mapper.toFact(emp);

        assertThat(fact).isNotNull();
    }

}
