package lk.ashan.routenetlkserverapllication.module.roster.event;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.CrewStatusRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RosterAssignmentConfirmedEventListener {

    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final ConductorRepository conductorRepository;
    private final CrewStatusRepository crewStatusRepository;


    @EventListener
    @Transactional
    public void handle(RosterAssignmentConfirmedEvent event) {

        Employee employee = employeeRepository.findById(event.employeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found")
                );

        CrewStatus activeStatus =
                crewStatusRepository.findByName("Active")
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Crew status not found")
                        );


        String designation = employee.getDesignation().getName();


        if ("Driver".equalsIgnoreCase(designation)) {

            Driver driver = driverRepository.findByEmployeeId(employee.getId())
                    .orElseThrow();

            driver.setCrewstatus(activeStatus);
            driverRepository.save(driver);

        } else if ("Conductor".equalsIgnoreCase(designation)) {

            System.out.println("conductor id is "+ String.valueOf(employee.getId()));

            Conductor conductor = conductorRepository.findByEmployeeId(employee.getId())
                    .orElseThrow();

            conductor.setCrewstatus(activeStatus);
            conductorRepository.save(conductor);
        }


        log.info(
                "Crew status updated to Active for employee {}",
                employee.getCallingname()+"-"+employee.getDesignation().getName()
        );
    }
}
