package lk.ashan.routenetlkserverapllication.module.crew.repository;


import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchStatusRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchTypeRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.RegionalOfficeRepository;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.employee.repository.*;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConductorRepositoryTest extends BaseTestContainer {

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchStatusRepository branchStatusRepository;

    @Autowired
    private BranchTypeRepository branchTypeRepository;

    @Autowired
    private RegionalOfficeRepository regionalOfficeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeStatusRepository employeeStatusRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private CrewStatusRepository crewStatusRepository;

    @Autowired
    private RouteFamiliarityLevelRepository routeFamiliarityLevelRepository;


    // ============================================================
    // findByEmployee_Branch_Id
    // ============================================================

    @Test
    void findByEmployeeBranchId_ShouldReturnConductors_WhenBranchHasConductors() {

        Branch branch = saveBranch("Dambulla", "DML0001");

        Employee employee1 = saveEmployee(branch);
        Employee employee2 = saveEmployee(branch);

        CrewStatus standby = crewStatusRepository.findById(1).orElseThrow();
        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1).orElseThrow();

        saveConductor("CON001", employee1, branch, standby, familiarityLevel);
        saveConductor("CON002", employee2, branch, standby, familiarityLevel);

        List<Conductor> result = conductorRepository.findByEmployee_Branch_Id(branch.getId());

        assertThat(result)
                .hasSize(2)
                .extracting(Conductor::getNumber)
                .containsExactlyInAnyOrder("CON001", "CON002");
    }

    @Test
    void findByEmployeeBranchId_ShouldReturnEmpty_WhenBranchHasNoConductors() {

        Branch branch = saveBranch("Colombo", "CLM0001");

        List<Conductor> result = conductorRepository.findByEmployee_Branch_Id(branch.getId());

        assertThat(result).isEmpty();
    }


    // ============================================================
    // countStandbyConductorsByBranch
    // ============================================================

    @Test
    void countStandbyConductorsByBranch_ShouldReturnStandbyCount() {

        Branch branch = saveBranch("Dambulla", "DML0002");

        Employee employee1 = saveEmployee(branch);
        Employee employee2 = saveEmployee(branch);
        Employee employee3 = saveEmployee(branch);

        CrewStatus eligible = crewStatusRepository.findById(1).orElseThrow();
        CrewStatus active = crewStatusRepository.findById(2).orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1).orElseThrow();

        saveConductor("CON101", employee1, branch, eligible, familiarityLevel);
        saveConductor("CON102", employee2, branch, eligible, familiarityLevel);
        saveConductor("CON103", employee3, branch, active, familiarityLevel);

        long result =
                conductorRepository.countStandbyConductorsByBranch(branch.getId());

        assertThat(result).isEqualTo(2);
    }

    @Test
    void countStandbyConductorsByBranch_ShouldReturnZero_WhenNoStandbyConductors() {

        Branch branch = saveBranch("Kandy", "KDY0001");

        long result = conductorRepository.countStandbyConductorsByBranch(branch.getId());

        assertThat(result).isZero();
    }


    // ============================================================
    // existsByEmployeeId
    // ============================================================

    @Test
    void existsByEmployeeId_ShouldReturnTrue_WhenEmployeeHasConductor() {

        Branch branch = saveBranch("Matale", "MAT0001");
        Employee employee = saveEmployee(branch);

        CrewStatus crewStatus = crewStatusRepository.findById(1).orElseThrow();
        RouteFamiliarityLevel familiarityLevel = routeFamiliarityLevelRepository.findById(1).orElseThrow();

        saveConductor("CON201", employee, branch, crewStatus, familiarityLevel);

        boolean result = conductorRepository.existsByEmployeeId(employee.getId());

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmployeeId_ShouldReturnFalse_WhenEmployeeHasNoConductor() {

        Branch branch = saveBranch("Galle", "GAL0001");
        Employee employee = saveEmployee(branch);

        boolean result = conductorRepository.existsByEmployeeId(employee.getId());

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByEmployeeIdAndIdNot
    // ============================================================

    @Test
    void existsByEmployeeIdAndIdNot_ShouldReturnTrue_WhenAnotherConductorUsesEmployee() {

        Branch branch = saveBranch("Kurunegala", "KUR0001");

        Employee employee1 = saveEmployee(branch);
        Employee employee2 = saveEmployee(branch);

        CrewStatus crewStatus = crewStatusRepository.findById(1).orElseThrow();
        RouteFamiliarityLevel familiarityLevel = routeFamiliarityLevelRepository.findById(1).orElseThrow();

        Conductor conductor1 = saveConductor("CON301", employee1, branch, crewStatus, familiarityLevel);
        saveConductor("CON302", employee2, branch, crewStatus, familiarityLevel);

        boolean result =
                conductorRepository.existsByEmployeeIdAndIdNot(
                        employee1.getId(),
                        conductor1.getId() + 1
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmployeeIdAndIdNot_ShouldReturnFalse_WhenOnlySameConductorMatches() {

        Branch branch = saveBranch("Negombo", "NEG0001");

        Employee employee = saveEmployee(branch);

        CrewStatus crewStatus = crewStatusRepository.findById(1).orElseThrow();
        RouteFamiliarityLevel familiarityLevel = routeFamiliarityLevelRepository.findById(1).orElseThrow();

        Conductor conductor = saveConductor("CON401", employee, branch, crewStatus, familiarityLevel);

        boolean result =
                conductorRepository.existsByEmployeeIdAndIdNot(
                        employee.getId(),
                        conductor.getId()
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // findByEmployeeId
    // ============================================================

    @Test
    void findByEmployeeId_ShouldReturnConductor_WhenEmployeeExists() {

        Branch branch = saveBranch("Anuradhapura", "ANU0001");
        Employee employee = saveEmployee(branch);

        CrewStatus crewStatus = crewStatusRepository.findById(1).orElseThrow();
        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1).orElseThrow();

        Conductor conductor =
                saveConductor("CON501", employee, branch, crewStatus, familiarityLevel);

        Optional<Conductor> result = conductorRepository.findByEmployeeId(employee.getId());

        assertThat(result)
                .isPresent()
                .get()
                .extracting(Conductor::getId)
                .isEqualTo(conductor.getId());
    }

    @Test
    void findByEmployeeId_ShouldReturnEmpty_WhenEmployeeHasNoConductor() {

        Branch branch = saveBranch("Badulla", "BAD0001");
        Employee employee = saveEmployee(branch);

        Optional<Conductor> result = conductorRepository.findByEmployeeId(employee.getId());

        assertThat(result).isEmpty();
    }


    // ============================================================
    // findAvailableConductors
    // ============================================================

    @Test
    void findAvailableConductors_ShouldReturnActiveConductorsOfBranch() {

        Branch branch = saveBranch("Ratnapura", "RAT0001");

        EmployeeStatus activeStatus = employeeStatusRepository.findById(1).orElseThrow();
        EmployeeStatus inactiveStatus = employeeStatusRepository.findById(2).orElseThrow();

        Employee activeEmployee = saveEmployee(branch, activeStatus);
        Employee inactiveEmployee = saveEmployee(branch, inactiveStatus);

        CrewStatus crewStatus = crewStatusRepository.findById(1).orElseThrow();
        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1).orElseThrow();

        saveConductor("CON601", activeEmployee, branch, crewStatus, familiarityLevel);
        saveConductor("CON602", inactiveEmployee, branch, crewStatus, familiarityLevel);

        List<Conductor> result = conductorRepository.findAvailableConductors(branch.getId());

        assertThat(result)
                .hasSize(1)
                .extracting(Conductor::getNumber)
                .containsExactly("CON601");
    }

    @Test
    void findAvailableConductors_ShouldReturnEmpty_WhenNoActiveConductors() {

        Branch branch = saveBranch("Jaffna", "JAF0001");

        List<Conductor> result = conductorRepository.findAvailableConductors(branch.getId());

        assertThat(result).isEmpty();
    }


    // ============================================================
    // Test Data Helpers (Only for Transactional Entities like Branch, Employee, Conductor)
    // ============================================================

    private Branch saveBranch(String name, String code) {
        BranchStatus branchStatus = branchStatusRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("BranchStatus not found in data.sql"));

        BranchType branchType = branchTypeRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("BranchType not found in data.sql"));

        RegionalOffice regionalOffice = regionalOfficeRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("RegionalOffice not found in data.sql"));

        Branch branch = Branch.builder()
                .name(name)
                .code(code)
                .address("Test Address")
                .telephone("0112345678")
                .email(code.toLowerCase() + "@test.com")
                .docreated(LocalDate.now())
                .branchstatus(branchStatus)
                .branchtype(branchType)
                .regionaloffice(regionalOffice)
                .build();

        return branchRepository.save(branch);
    }

    private Employee saveEmployee(Branch branch) {
        EmployeeStatus status = employeeStatusRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("EmployeeStatus not found in data.sql"));
        return saveEmployee(branch, status);
    }

    private Employee saveEmployee(Branch branch, EmployeeStatus status) {
        Gender gender = genderRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Gender not found in data.sql"));

        Department department = departmentRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Department not found in data.sql"));

        Designation designation = designationRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Designation not found in data.sql"));

        EmployeeType employeeType = employeeTypeRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("EmployeeType not found in data.sql"));

        long uniqueId = System.nanoTime();

        String uniqueNic = "90" + String.format("%07d", uniqueId % 10000000) + "V";
        String empNumber = "EMP" + (uniqueId % 100000);

        Employee employee = Employee.builder()
                .number(empNumber)
                .fullname("Test Employee")
                .callingname("Test")
                .nic(uniqueNic)
                .gender(gender)
                .mobile("077" + String.format("%07d", uniqueId % 10000000))
                .email("employee" + uniqueId + "@test.com")
                .address("Test Address")
                .emergencycontact("0712345678")
                .branch(branch)
                .department(department)
                .designation(designation)
                .employeetype(employeeType)
                .doj(LocalDate.now())
                .employeestatus(status)
                .build();

        return employeeRepository.save(employee);
    }

    private Conductor saveConductor(
            String number,
            Employee employee,
            Branch branch,
            CrewStatus crewStatus,
            RouteFamiliarityLevel familiarityLevel
    ) {
        Conductor conductor = Conductor.builder()
                .number(number)
                .employee(employee)
                .branch(branch)
                .crewstatus(crewStatus)
                .routefamiliaritylevel(familiarityLevel)
                .domedicalissued(LocalDate.now().minusMonths(6))
                .domedicalexpired(LocalDate.now().plusMonths(6))
                .build();

        return conductorRepository.save(conductor);
    }
}
