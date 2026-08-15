package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchStatusRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchTypeRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.RegionalOfficeRepository;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.crew.repository.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.projection.EmployeeFamiliarityProjection;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest extends BaseTestContainer {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchStatusRepository branchStatusRepository;

    @Autowired
    private BranchTypeRepository branchTypeRepository;

    @Autowired
    private RegionalOfficeRepository regionalOfficeRepository;

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
    private DriverRepository driverRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private CrewStatusRepository crewStatusRepository;

    @Autowired
    private RouteFamiliarityLevelRepository routeFamiliarityLevelRepository;

    @Autowired
    private LicenseCategoryRepository licenseCategoryRepository;


    // ============================================================
    // existsByNic
    // ============================================================

    @Test
    void existsByNic_ShouldReturnTrue_WhenNicExists() {

        Branch branch = saveBranch("Dambulla", "DML0001");

        Employee employee = saveEmployee(
                branch,
                "EMP001",
                "901234567V",
                "0771234567",
                "0711234567"
        );

        boolean result =
                employeeRepository.existsByNic(employee.getNic());

        assertThat(result).isTrue();
    }


    @Test
    void existsByNic_ShouldReturnFalse_WhenNicDoesNotExist() {

        boolean result =
                employeeRepository.existsByNic("999999999V");

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByMobile
    // ============================================================

    @Test
    void existsByMobile_ShouldReturnTrue_WhenMobileExists() {

        Branch branch = saveBranch("Colombo", "CLM0001");

        Employee employee = saveEmployee(
                branch,
                "EMP002",
                "901234568V",
                "0772222222",
                "0712222222"
        );

        boolean result =
                employeeRepository.existsByMobile(employee.getMobile());

        assertThat(result).isTrue();
    }


    @Test
    void existsByMobile_ShouldReturnFalse_WhenMobileDoesNotExist() {

        boolean result =
                employeeRepository.existsByMobile("0779999999");

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByEmergencycontact
    // ============================================================

    @Test
    void existsByEmergencycontact_ShouldReturnTrue_WhenEmergencyContactExists() {

        Branch branch = saveBranch("Kandy", "KDY0001");

        Employee employee = saveEmployee(
                branch,
                "EMP003",
                "901234569V",
                "0773333333",
                "0713333333"
        );

        boolean result =
                employeeRepository.existsByEmergencycontact(
                        employee.getEmergencycontact()
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByEmergencycontact_ShouldReturnFalse_WhenEmergencyContactDoesNotExist() {

        boolean result =
                employeeRepository.existsByEmergencycontact("0719999999");

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByNicAndIdNot
    // ============================================================

    @Test
    void existsByNicAndIdNot_ShouldReturnTrue_WhenAnotherEmployeeUsesNic() {

        Branch branch = saveBranch("Matale", "MAT0001");

        Employee employee = saveEmployee(
                branch,
                "EMP004",
                "901234570V",
                "0774444444",
                "0714444444"
        );

        boolean result =
                employeeRepository.existsByNicAndIdNot(
                        employee.getNic(),
                        employee.getId() + 1
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByNicAndIdNot_ShouldReturnFalse_WhenOnlySameEmployeeMatches() {

        Branch branch = saveBranch("Galle", "GAL0001");

        Employee employee = saveEmployee(
                branch,
                "EMP005",
                "901234571V",
                "0775555555",
                "0715555555"
        );

        boolean result =
                employeeRepository.existsByNicAndIdNot(
                        employee.getNic(),
                        employee.getId()
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByMobileAndIdNot
    // ============================================================

    @Test
    void existsByMobileAndIdNot_ShouldReturnTrue_WhenAnotherEmployeeUsesMobile() {

        Branch branch = saveBranch("Kurunegala", "KUR0001");

        Employee employee = saveEmployee(
                branch,
                "EMP006",
                "901234572V",
                "0776666666",
                "0716666666"
        );

        boolean result =
                employeeRepository.existsByMobileAndIdNot(
                        employee.getMobile(),
                        employee.getId() + 1
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByMobileAndIdNot_ShouldReturnFalse_WhenOnlySameEmployeeMatches() {

        Branch branch = saveBranch("Negombo", "NEG0001");

        Employee employee = saveEmployee(
                branch,
                "EMP007",
                "901234573V",
                "0777777777",
                "0717777777"
        );

        boolean result =
                employeeRepository.existsByMobileAndIdNot(
                        employee.getMobile(),
                        employee.getId()
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByEmergencycontactAndIdNot
    // ============================================================

    @Test
    void existsByEmergencycontactAndIdNot_ShouldReturnTrue_WhenAnotherEmployeeUsesEmergencyContact() {

        Branch branch = saveBranch("Ratnapura", "RAT0001");

        Employee employee = saveEmployee(
                branch,
                "EMP008",
                "901234574V",
                "0778888888",
                "0718888888"
        );

        boolean result =
                employeeRepository.existsByEmergencycontactAndIdNot(
                        employee.getEmergencycontact(),
                        employee.getId() + 1
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByEmergencycontactAndIdNot_ShouldReturnFalse_WhenOnlySameEmployeeMatches() {

        Branch branch = saveBranch("Jaffna", "JAF0001");

        Employee employee = saveEmployee(
                branch,
                "EMP009",
                "901234575V",
                "0779999999",
                "0719999999"
        );

        boolean result =
                employeeRepository.existsByEmergencycontactAndIdNot(
                        employee.getEmergencycontact(),
                        employee.getId()
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // removeAll
    // ============================================================

    @Test
    void removeAll_ShouldMarkSelectedEmployeesAsDeleted() {

        Branch branch = saveBranch("Anuradhapura", "ANU0001");

        Employee employee1 = saveEmployee(
                branch,
                "EMP101",
                "901234576V",
                "0701111111",
                "0721111111"
        );



        Employee employee2 = saveEmployee(
                branch,
                "EMP102",
                "901234577V",
                "0702222222",
                "0722222222"
        );

        employeeRepository.removeAll(
                List.of(employee1.getId(), employee2.getId())
        );

        employeeRepository.flush();

        Employee result1 =
                employeeRepository.findById(employee1.getId())
                        .orElseThrow();

        Employee result2 =
                employeeRepository.findById(employee2.getId())
                        .orElseThrow();

        assertThat(result1.isDeleted()).isTrue();
        assertThat(result2.isDeleted()).isTrue();
    }


    // ============================================================
    // restoreAll
    // ============================================================

    @Test
    void restoreAll_ShouldMarkSelectedEmployeesAsNotDeleted() {

        Branch branch = saveBranch("Badulla", "BAD0001");

        Employee employee1 = saveEmployee(
                branch,
                "EMP103",
                "901234578V",
                "0703333333",
                "0723333333"
        );

        Employee employee2 = saveEmployee(
                branch,
                "EMP104",
                "901234579V",
                "0704444444",
                "0724444444"
        );

        employeeRepository.removeAll(
                List.of(employee1.getId(), employee2.getId())
        );

        employeeRepository.flush();

        employeeRepository.restoreAll(
                List.of(employee1.getId(), employee2.getId())
        );

        employeeRepository.flush();

        Employee result1 =
                employeeRepository.findById(employee1.getId())
                        .orElseThrow();

        Employee result2 =
                employeeRepository.findById(employee2.getId())
                        .orElseThrow();

        assertThat(result1.isDeleted()).isFalse();
        assertThat(result2.isDeleted()).isFalse();
    }


    // ============================================================
    // findEmployeesWithoutDriver
    // ============================================================

    @Test
    void findEmployeesWithoutDriver_ShouldReturnEmployeesWithoutDriver() {

        Branch branch = saveBranch("Colombo", "CLM0002");

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employeeWithoutDriver =
                saveEmployee(
                        branch,
                        "EMP201",
                        "901234580V",
                        "0705555555",
                        "0725555555",
                        designation
                );

        Employee employeeWithDriver =
                saveEmployee(
                        branch,
                        "EMP202",
                        "901234581V",
                        "0706666666",
                        "0726666666",
                        designation
                );

        saveDriver(employeeWithDriver, branch);

        List<Employee> result =
                employeeRepository.findEmployeesWithoutDriver(
                        designation.getName()
                );

        assertThat(result.stream()
                .map(Employee::getId)
                .toList())
                .contains(employeeWithoutDriver.getId())
                .doesNotContain(employeeWithDriver.getId());
    }


    @Test
    void findEmployeesWithoutDriver_ShouldReturnEmpty_WhenAllEmployeesHaveDrivers() {

        Branch branch = saveBranch("Kandy", "KDY0002");

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employee =
                saveEmployee(
                        branch,
                        "EMP203",
                        "901234582V",
                        "0707777777",
                        "0727777777",
                        designation
                );

        saveDriver(employee, branch);

        List<Employee> result =
                employeeRepository.findEmployeesWithoutDriver(
                        designation.getName()
                );

        assertThat(result)
                .extracting(Employee::getId)
                .doesNotContain(employee.getId());
    }


    // ============================================================
    // findEmployeesWithoutConductor
    // ============================================================

    @Test
    void findEmployeesWithoutConductor_ShouldReturnEmployeesWithoutConductor() {

        Branch branch = saveBranch("Galle", "GAL0002");

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employeeWithoutConductor =
                saveEmployee(
                        branch,
                        "EMP301",
                        "901234583V",
                        "0708888888",
                        "0728888888",
                        designation
                );

        Employee employeeWithConductor =
                saveEmployee(
                        branch,
                        "EMP302",
                        "901234584V",
                        "0709999999",
                        "0729999999",
                        designation
                );

        saveConductor(employeeWithConductor, branch);

        List<Employee> result =
                employeeRepository.findEmployeesWithoutConductor(
                        designation.getName()
                );

        assertThat(result)
                .extracting(Employee::getId)
                .contains(employeeWithoutConductor.getId())
                .doesNotContain(employeeWithConductor.getId());
    }


    @Test
    void findEmployeesWithoutConductor_ShouldReturnEmpty_WhenAllEmployeesHaveConductors() {

        Branch branch = saveBranch("Matara", "MAT0002");

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employee =
                saveEmployee(
                        branch,
                        "EMP303",
                        "901234585V",
                        "0701212121",
                        "0721212121",
                        designation
                );

        saveConductor(employee, branch);

        List<Employee> result =
                employeeRepository.findEmployeesWithoutConductor(
                        designation.getName()
                );

        assertThat(result)
                .extracting(Employee::getId)
                .doesNotContain(employee.getId());
    }


    // ============================================================
    // findActiveEmployeesWithFamiliarity
    // ============================================================

    @Test
    void findActiveEmployeesWithFamiliarity_ShouldReturnActiveEmployeesOfBranch() {

        Branch branch = saveBranch("Dambulla", "DML0003");

        EmployeeStatus activeStatus =
                employeeStatusRepository.findById(1)
                        .orElseThrow();

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employee =
                saveEmployee(
                        branch,
                        "EMP401",
                        "901234586V",
                        "0701313131",
                        "0721313131",
                        designation,
                        activeStatus
                );

        List<EmployeeFamiliarityProjection> result =
                employeeRepository.findActiveEmployeesWithFamiliarity(
                        branch.getId(),
                        List.of(designation.getId())
                );

        assertThat(result)
                .extracting(EmployeeFamiliarityProjection::getId)
                .contains(employee.getId());
    }


    @Test
    void findActiveEmployeesWithFamiliarity_ShouldReturnEmpty_WhenEmployeeIsInactive() {

        Branch branch = saveBranch("Kandy", "KDY0003");

        EmployeeStatus inactiveStatus =
                employeeStatusRepository.findById(2)
                        .orElseThrow();

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employee =
                saveEmployee(
                        branch,
                        "EMP402",
                        "901234587V",
                        "0701414141",
                        "0721414141",
                        designation,
                        inactiveStatus
                );

        List<EmployeeFamiliarityProjection> result =
                employeeRepository.findActiveEmployeesWithFamiliarity(
                        branch.getId(),
                        List.of(designation.getId())
                );

        assertThat(result)
                .extracting(EmployeeFamiliarityProjection::getId)
                .doesNotContain(employee.getId());
    }


    @Test
    void findActiveEmployeesWithFamiliarity_ShouldReturnDriverFamiliarity() {

        Branch branch = saveBranch("Dambulla", "DML0004");

        EmployeeStatus activeStatus =
                employeeStatusRepository.findById(1)
                        .orElseThrow();

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employee =
                saveEmployee(
                        branch,
                        "EMP403",
                        "901234588V",
                        "0701515151",
                        "0721515151",
                        designation,
                        activeStatus
                );

        saveDriver(employee, branch);

        List<EmployeeFamiliarityProjection> result =
                employeeRepository.findActiveEmployeesWithFamiliarity(
                        branch.getId(),
                        List.of(designation.getId())
                );

        assertThat(result)
                .hasSize(1);

        EmployeeFamiliarityProjection projection = result.get(0);

        assertThat(projection.getId())
                .isEqualTo(employee.getId());

        assertThat(projection.getFamiliarityLevel())
                .isEqualTo(1);
    }


    @Test
    void findActiveEmployeesWithFamiliarity_ShouldReturnConductorFamiliarity() {

        Branch branch = saveBranch("Dambulla", "DML0005");

        EmployeeStatus activeStatus =
                employeeStatusRepository.findById(1)
                        .orElseThrow();

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow();

        Employee employee =
                saveEmployee(
                        branch,
                        "EMP404",
                        "901234589V",
                        "0701616161",
                        "0721616161",
                        designation,
                        activeStatus
                );

        saveConductor(employee, branch);

        List<EmployeeFamiliarityProjection> result =
                employeeRepository.findActiveEmployeesWithFamiliarity(
                        branch.getId(),
                        List.of(designation.getId())
                );

        assertThat(result)
                .hasSize(1);

        EmployeeFamiliarityProjection projection = result.get(0);

        assertThat(projection.getId())
                .isEqualTo(employee.getId());

        assertThat(projection.getFamiliarityLevel())
                .isEqualTo(1);
    }


    // ============================================================
    // Test Data Helpers
    // ============================================================

    private Branch saveBranch(String name, String code) {

        BranchStatus branchStatus =
                branchStatusRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "BranchStatus not found in data.sql"
                                ));

        BranchType branchType =
                branchTypeRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "BranchType not found in data.sql"
                                ));

        RegionalOffice regionalOffice =
                regionalOfficeRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "RegionalOffice not found in data.sql"
                                ));

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


    private Employee saveEmployee(
            Branch branch,
            String number,
            String nic,
            String mobile,
            String emergencyContact
    ) {

        EmployeeStatus status =
                employeeStatusRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "EmployeeStatus not found in data.sql"
                                ));

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Designation not found in data.sql"
                                ));

        return saveEmployee(
                branch,
                number,
                nic,
                mobile,
                emergencyContact,
                designation,
                status
        );
    }


    private Employee saveEmployee(
            Branch branch,
            String number,
            String nic,
            String mobile,
            String emergencyContact,
            Designation designation
    ) {

        EmployeeStatus status =
                employeeStatusRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "EmployeeStatus not found in data.sql"
                                ));

        return saveEmployee(
                branch,
                number,
                nic,
                mobile,
                emergencyContact,
                designation,
                status
        );
    }


    private Employee saveEmployee(
            Branch branch,
            String number,
            String nic,
            String mobile,
            String emergencyContact,
            Designation designation,
            EmployeeStatus status
    ) {

        Gender gender =
                genderRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Gender not found in data.sql"
                                ));

        Department department =
                departmentRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department not found in data.sql"
                                ));

        EmployeeType employeeType =
                employeeTypeRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "EmployeeType not found in data.sql"
                                ));

        Employee employee = Employee.builder()
                .number(number)
                .fullname("Test Employee")
                .callingname("Test")
                .nic(nic)
                .gender(gender)
                .mobile(mobile)
                .email(number.toLowerCase() + "@test.com")
                .address("Test Address")
                .emergencycontact(emergencyContact)
                .branch(branch)
                .department(department)
                .designation(designation)
                .employeetype(employeeType)
                .doj(LocalDate.now())
                .employeestatus(status)
                .build();

        return employeeRepository.save(employee);
    }


    private Employee saveEmployee(Branch branch) {

        EmployeeStatus status =
                employeeStatusRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "EmployeeStatus not found in data.sql"
                                ));

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Designation not found in data.sql"
                                ));

        long uniqueId = System.nanoTime();

        return saveEmployee(
                branch,
                "EMP" + uniqueId,
                "90" + String.format("%07d", uniqueId % 10000000) + "V",
                "077" + String.format("%07d", uniqueId % 10000000),
                "071" + String.format("%07d", uniqueId % 10000000),
                designation,
                status
        );
    }


    private Employee saveEmployee(
            Branch branch,
            EmployeeStatus status
    ) {

        Designation designation =
                designationRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Designation not found in data.sql"
                                ));

        long uniqueId = System.nanoTime();

        return saveEmployee(
                branch,
                "EMP" + uniqueId,
                "90" + String.format("%07d", uniqueId % 10000000) + "V",
                "077" + String.format("%07d", uniqueId % 10000000),
                "071" + String.format("%07d", uniqueId % 10000000),
                designation,
                status
        );
    }


    // ============================================================
    // Driver Test Data
    // ============================================================

    private Driver saveDriver(
            Employee employee,
            Branch branch
    ) {

        CrewStatus crewStatus =
                crewStatusRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "CrewStatus not found in data.sql"
                                ));

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "RouteFamiliarityLevel not found in data.sql"
                                ));

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "LicenseCategory not found in data.sql"
                                ));

        long uniqueId = System.nanoTime();

        Driver driver = Driver.builder()
                .number("DRV" +  (uniqueId % 100000))
                .totaldutyminute(0)
                .licensenumber("B" + (uniqueId % 100000000))
                .dolicenseissued(LocalDate.now().minusYears(1))
                .dolicenseexpired(LocalDate.now().plusYears(1))
                .domedicalissued(LocalDate.now().minusMonths(6))
                .domedicalexpired(LocalDate.now().plusMonths(6))
                .employee(employee)
                .licensecategory(licenseCategory)
                .crewstatus(crewStatus)
                .routefamiliaritylevel(familiarityLevel)
                .branch(branch)
                .build();

        return driverRepository.save(driver);
    }


    // ============================================================
    // Conductor Test Data
    // ============================================================

    private Conductor saveConductor(
            Employee employee,
            Branch branch
    ) {

        CrewStatus crewStatus =
                crewStatusRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "CrewStatus not found in data.sql"
                                ));

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "RouteFamiliarityLevel not found in data.sql"
                                ));

        long uniqueId = System.nanoTime();

        Conductor conductor = Conductor.builder()
                .number("CON" +  (uniqueId % 100000))
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
