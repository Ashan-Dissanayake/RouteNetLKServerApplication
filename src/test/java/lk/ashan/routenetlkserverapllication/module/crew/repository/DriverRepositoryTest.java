package lk.ashan.routenetlkserverapllication.module.crew.repository;


import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchStatusRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchTypeRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.RegionalOfficeRepository;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.LicenseCategory;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DriverRepositoryTest extends BaseTestContainer {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchStatusRepository branchStatusRepository;

    @Autowired
    private BranchTypeRepository branchTypeRepository;

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

    @Autowired
    private LicenseCategoryRepository licenseCategoryRepository;

    @Autowired
    private RegionalOfficeRepository regionalOfficeRepository;


    // ============================================================
    // findByEmployee_Branch_Id
    // ============================================================

    @Test
    void findByEmployeeBranchId_ShouldReturnDrivers_WhenBranchHasDrivers() {

        Branch branch = saveBranch("Dambulla", "DML0001");

        Employee employee1 = saveEmployee(branch);
        Employee employee2 = saveEmployee(branch);

        CrewStatus crewStatus = crewStatusRepository.findById(1)
                .orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow();

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow();

        saveDriver(
                "DRV001",
                "B1234567",
                employee1,
                branch,
                crewStatus,
                familiarityLevel,
                licenseCategory
        );

        saveDriver(
                "DRV002",
                "B7654321",
                employee2,
                branch,
                crewStatus,
                familiarityLevel,
                licenseCategory
        );

        List<Driver> result =
                driverRepository.findByEmployee_Branch_Id(branch.getId());

        assertThat(result)
                .hasSize(2)
                .extracting(Driver::getNumber)
                .containsExactlyInAnyOrder("DRV001", "DRV002");
    }


    @Test
    void findByEmployeeBranchId_ShouldReturnEmpty_WhenBranchHasNoDrivers() {

        Branch branch = saveBranch("Colombo", "CLM0001");

        List<Driver> result =
                driverRepository.findByEmployee_Branch_Id(branch.getId());

        assertThat(result).isEmpty();
    }


    // ============================================================
    // countStandbyDriversByBranch
    // ============================================================

    @Test
    void countStandbyDriversByBranch_ShouldReturnStandbyCount() {

        Branch branch = saveBranch("Dambulla", "DML0002");

        Employee employee1 = saveEmployee(branch);
        Employee employee2 = saveEmployee(branch);
        Employee employee3 = saveEmployee(branch);

        CrewStatus eligible = crewStatusRepository.findById(1)
                .orElseThrow();

        CrewStatus active = crewStatusRepository.findById(2)
                .orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow();

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow();

        saveDriver(
                "DRV101",
                "B1000001",
                employee1,
                branch,
                eligible,
                familiarityLevel,
                licenseCategory
        );

        saveDriver(
                "DRV102",
                "B1000002",
                employee2,
                branch,
                eligible,
                familiarityLevel,
                licenseCategory
        );

        saveDriver(
                "DRV103",
                "B1000003",
                employee3,
                branch,
                active,
                familiarityLevel,
                licenseCategory
        );

        long result = driverRepository.countStandbyDriversByBranch(branch.getId());

        assertThat(result).isEqualTo(2);
    }


    @Test
    void countStandbyDriversByBranch_ShouldReturnZero_WhenNoStandbyDrivers() {

        Branch branch = saveBranch("Kandy", "KDY0001");

        long result =
                driverRepository.countStandbyDriversByBranch(branch.getId());

        assertThat(result).isZero();
    }


    // ============================================================
    // existsByEmployeeId
    // ============================================================

    @Test
    void existsByEmployeeId_ShouldReturnTrue_WhenEmployeeHasDriver() {

        Branch branch = saveBranch("Matale", "MAT0001");

        Employee employee = saveEmployee(branch);

        CrewStatus crewStatus =
                crewStatusRepository.findById(1)
                        .orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow();

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow();

        saveDriver(
                "DRV201",
                "B2000001",
                employee,
                branch,
                crewStatus,
                familiarityLevel,
                licenseCategory
        );

        boolean result =
                driverRepository.existsByEmployeeId(employee.getId());

        assertThat(result).isTrue();
    }


    @Test
    void existsByEmployeeId_ShouldReturnFalse_WhenEmployeeHasNoDriver() {

        Branch branch = saveBranch("Galle", "GAL0001");

        Employee employee = saveEmployee(branch);

        boolean result =
                driverRepository.existsByEmployeeId(employee.getId());

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByEmployeeIdAndIdNot
    // ============================================================

    @Test
    void existsByEmployeeIdAndIdNot_ShouldReturnTrue_WhenAnotherDriverUsesEmployee() {

        Branch branch = saveBranch("Kurunegala", "KUR0001");

        Employee employee = saveEmployee(branch);

        CrewStatus crewStatus =
                crewStatusRepository.findById(1)
                        .orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow();

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow();

        Driver driver =
                saveDriver(
                        "DRV301",
                        "B3000001",
                        employee,
                        branch,
                        crewStatus,
                        familiarityLevel,
                        licenseCategory
                );

        boolean result =
                driverRepository.existsByEmployeeIdAndIdNot(
                        employee.getId(),
                        driver.getId() + 1
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByEmployeeIdAndIdNot_ShouldReturnFalse_WhenOnlySameDriverMatches() {

        Branch branch = saveBranch("Negombo", "NEG0001");

        Employee employee = saveEmployee(branch);

        CrewStatus crewStatus =
                crewStatusRepository.findById(1)
                        .orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow();

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow();

        Driver driver =
                saveDriver(
                        "DRV401",
                        "B4000001",
                        employee,
                        branch,
                        crewStatus,
                        familiarityLevel,
                        licenseCategory
                );

        boolean result =
                driverRepository.existsByEmployeeIdAndIdNot(
                        employee.getId(),
                        driver.getId()
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // findByEmployeeId
    // ============================================================

    @Test
    void findByEmployeeId_ShouldReturnDriver_WhenEmployeeExists() {

        Branch branch = saveBranch("Anuradhapura", "ANU0001");

        Employee employee = saveEmployee(branch);

        CrewStatus crewStatus =
                crewStatusRepository.findById(1)
                        .orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow();

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow();

        Driver driver =
                saveDriver(
                        "DRV501",
                        "B5000001",
                        employee,
                        branch,
                        crewStatus,
                        familiarityLevel,
                        licenseCategory
                );

        Optional<Driver> result =
                driverRepository.findByEmployeeId(employee.getId());

        assertThat(result)
                .isPresent()
                .get()
                .extracting(Driver::getId)
                .isEqualTo(driver.getId());
    }


    @Test
    void findByEmployeeId_ShouldReturnEmpty_WhenEmployeeHasNoDriver() {

        Branch branch = saveBranch("Badulla", "BAD0001");

        Employee employee = saveEmployee(branch);

        Optional<Driver> result =
                driverRepository.findByEmployeeId(employee.getId());

        assertThat(result).isEmpty();
    }


    // ============================================================
    // existsByLicensenumber
    // ============================================================

    @Test
    void existsByLicensenumber_ShouldReturnTrue_WhenLicenseNumberExists() {

        Branch branch = saveBranch("Ratnapura", "RAT0001");

        Employee employee = saveEmployee(branch);

        Driver driver = saveDriverWithDefaults(
                "DRV601",
                "B6000001",
                employee,
                branch
        );

        boolean result =
                driverRepository.existsByLicensenumber(
                        driver.getLicensenumber()
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByLicensenumber_ShouldReturnFalse_WhenLicenseNumberDoesNotExist() {

        boolean result =
                driverRepository.existsByLicensenumber("NONEXISTENT");

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByNumber
    // ============================================================

    @Test
    void existsByNumber_ShouldReturnTrue_WhenDriverNumberExists() {

        Branch branch = saveBranch("Jaffna", "JAF0001");

        Employee employee = saveEmployee(branch);

        saveDriverWithDefaults(
                "DRV701",
                "B7000001",
                employee,
                branch
        );

        boolean result =
                driverRepository.existsByNumber("DRV701");

        assertThat(result).isTrue();
    }


    @Test
    void existsByNumber_ShouldReturnFalse_WhenDriverNumberDoesNotExist() {

        boolean result =
                driverRepository.existsByNumber("NONEXISTENT");

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByLicensenumberAndIdNot
    // ============================================================

    @Test
    void existsByLicensenumberAndIdNot_ShouldReturnTrue_WhenAnotherDriverUsesLicense() {

        Branch branch = saveBranch("Kegalle", "KEG0001");

        Employee employee = saveEmployee(branch);

        Driver driver = saveDriverWithDefaults(
                "DRV801",
                "B8000001",
                employee,
                branch
        );

        boolean result =
                driverRepository.existsByLicensenumberAndIdNot(
                        driver.getLicensenumber(),
                        driver.getId() + 1
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByLicensenumberAndIdNot_ShouldReturnFalse_WhenOnlySameDriverMatches() {

        Branch branch = saveBranch("Monaragala", "MON0001");

        Employee employee = saveEmployee(branch);

        Driver driver = saveDriverWithDefaults(
                "DRV802",
                "B8000002",
                employee,
                branch
        );

        boolean result =
                driverRepository.existsByLicensenumberAndIdNot(
                        driver.getLicensenumber(),
                        driver.getId()
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // existsByNumberAndIdNot
    // ============================================================

    @Test
    void existsByNumberAndIdNot_ShouldReturnTrue_WhenAnotherDriverUsesNumber() {

        Branch branch = saveBranch("Polonnaruwa", "POL0001");

        Employee employee = saveEmployee(branch);

        Driver driver = saveDriverWithDefaults(
                "DRV901",
                "B9000001",
                employee,
                branch
        );

        boolean result =
                driverRepository.existsByNumberAndIdNot(
                        driver.getNumber(),
                        driver.getId() + 1
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByNumberAndIdNot_ShouldReturnFalse_WhenOnlySameDriverMatches() {

        Branch branch = saveBranch("Hambantota", "HAM0001");

        Employee employee = saveEmployee(branch);

        Driver driver = saveDriverWithDefaults(
                "DRV902",
                "B9000002",
                employee,
                branch
        );

        boolean result =
                driverRepository.existsByNumberAndIdNot(
                        driver.getNumber(),
                        driver.getId()
                );

        assertThat(result).isFalse();
    }


    // ============================================================
    // findAvailableDrivers
    // ============================================================

    @Test
    void findAvailableDrivers_ShouldReturnActiveDriversOfBranch() {

        Branch branch = saveBranch("Colombo", "CLM0002");

        EmployeeStatus activeStatus =
                employeeStatusRepository.findById(1)
                        .orElseThrow();

        EmployeeStatus inactiveStatus =
                employeeStatusRepository.findById(2)
                        .orElseThrow();

        Employee activeEmployee =
                saveEmployee(branch, activeStatus);

        Employee inactiveEmployee =
                saveEmployee(branch, inactiveStatus);

        Driver activeDriver =
                saveDriverWithDefaults(
                        "DRV1001",
                        "B10000001",
                        activeEmployee,
                        branch
                );

        saveDriverWithDefaults(
                "DRV1002",
                "B10000002",
                inactiveEmployee,
                branch
        );

        List<Driver> result =
                driverRepository.findAvailableDrivers(branch.getId());

        assertThat(result)
                .hasSize(1)
                .extracting(Driver::getNumber)
                .containsExactly(activeDriver.getNumber());
    }


    @Test
    void findAvailableDrivers_ShouldReturnEmpty_WhenNoActiveDrivers() {

        Branch branch = saveBranch("Matara", "MAT0002");

        List<Driver> result =
                driverRepository.findAvailableDrivers(branch.getId());

        assertThat(result).isEmpty();
    }


    // ============================================================
    // Test Data Helpers
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

        EmployeeStatus status =
                employeeStatusRepository.findById(1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "EmployeeStatus not found in data.sql"
                                ));

        return saveEmployee(branch, status);
    }


    private Employee saveEmployee(
            Branch branch,
            EmployeeStatus status
    ) {

        var gender = genderRepository.findById(1)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Gender not found in data.sql"
                        ));

        var department = departmentRepository.findById(1)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Department not found in data.sql"
                        ));

        var designation = designationRepository.findById(1)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Designation not found in data.sql"
                        ));

        var employeeType = employeeTypeRepository.findById(1)
                .orElseThrow(() ->
                        new RuntimeException(
                                "EmployeeType not found in data.sql"
                        ));

        long uniqueId = System.nanoTime();

        String uniqueNic =
                "90" + String.format(
                        "%07d",
                        uniqueId % 10000000
                ) + "V";

        String empNumber =
                "EMP" + (uniqueId % 100000);

        Employee employee = Employee.builder()
                .number(empNumber)
                .fullname("Test Employee")
                .callingname("Test")
                .nic(uniqueNic)
                .gender(gender)
                .mobile(
                        "077" + String.format(
                                "%07d",
                                uniqueId % 10000000
                        )
                )
                .email(
                        "employee" +
                                uniqueId +
                                "@test.com"
                )
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


    private Driver saveDriverWithDefaults(
            String number,
            String licenseNumber,
            Employee employee,
            Branch branch
    ) {

        CrewStatus crewStatus =
                crewStatusRepository.findById(1)
                        .orElseThrow();

        RouteFamiliarityLevel familiarityLevel =
                routeFamiliarityLevelRepository.findById(1)
                        .orElseThrow();

        LicenseCategory licenseCategory =
                licenseCategoryRepository.findById(1)
                        .orElseThrow();

        return saveDriver(
                number,
                licenseNumber,
                employee,
                branch,
                crewStatus,
                familiarityLevel,
                licenseCategory
        );
    }


    private Driver saveDriver(
            String number,
            String licenseNumber,
            Employee employee,
            Branch branch,
            CrewStatus crewStatus,
            RouteFamiliarityLevel familiarityLevel,
            LicenseCategory licenseCategory
    ) {

        Driver driver = Driver.builder()
                .number(number)
                .totaldutyminute(0)
                .licensenumber(licenseNumber)
                .dolicenseissued(
                        LocalDate.now().minusYears(1)
                )
                .dolicenseexpired(
                        LocalDate.now().plusYears(1)
                )
                .domedicalissued(
                        LocalDate.now().minusMonths(6)
                )
                .domedicalexpired(
                        LocalDate.now().plusMonths(6)
                )
                .employee(employee)
                .licensecategory(licenseCategory)
                .crewstatus(crewStatus)
                .routefamiliaritylevel(familiarityLevel)
                .branch(branch)
                .build();

        return driverRepository.save(driver);
    }
}
