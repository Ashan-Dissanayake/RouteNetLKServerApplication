package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
class BranchRepositoryTest extends BaseTestContainer {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchTypeRepository branchTypeRepository;

    @Autowired
    private BranchStatusRepository branchStatusRepository;

    @Autowired
    private RegionalOfficeRepository regionalOfficeRepository;


    // ==================== existsByCodeEqualsIgnoreCase ====================

    @Test
    void existsByCodeEqualsIgnoreCase_ShouldReturnTrue_WhenCodeExists() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByCodeEqualsIgnoreCase(
                        branch.getCode()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByCodeEqualsIgnoreCase_ShouldReturnFalse_WhenCodeDoesNotExist() {

        boolean result =
                branchRepository.existsByCodeEqualsIgnoreCase("UNKNOWN");

        assertThat(result).isFalse();
    }


    // ==================== existsByNameEqualsIgnoreCase ====================

    @Test
    void existsByNameEqualsIgnoreCase_ShouldReturnTrue_WhenNameExists() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByNameEqualsIgnoreCase(
                        branch.getName().toLowerCase()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByNameEqualsIgnoreCase_ShouldReturnFalse_WhenNameDoesNotExist() {

        boolean result =
                branchRepository.existsByNameEqualsIgnoreCase(
                        "Unknown Branch"
                );

        assertThat(result).isFalse();
    }


    // ==================== existsByNameEqualsIgnoreCaseAndIdNot ====================

    @Test
    void existsByNameEqualsIgnoreCaseAndIdNot_ShouldReturnTrue_WhenAnotherBranchHasSameName() {

        Branch first = createAndSaveBranch();

        Branch second = createBranch(
                "Colombo",
                "CLM0001",
                "Colombo",
                "0112345678",
                "clm@sltb.lk"
        );

        second = branchRepository.save(second);

        boolean result =
                branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                        first.getName(),
                        second.getId()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByNameEqualsIgnoreCaseAndIdNot_ShouldReturnFalse_WhenSameBranchIdIsExcluded() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                        branch.getName(),
                        branch.getId()
                );

        assertThat(result).isFalse();
    }


    // ==================== existsByEmailEqualsIgnoreCase ====================

    @Test
    void existsByEmailEqualsIgnoreCase_ShouldReturnTrue_WhenEmailExists() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByEmailEqualsIgnoreCase(
                        branch.getEmail().toUpperCase()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmailEqualsIgnoreCase_ShouldReturnFalse_WhenEmailDoesNotExist() {

        boolean result =
                branchRepository.existsByEmailEqualsIgnoreCase(
                        "unknown@sltb.lk"
                );

        assertThat(result).isFalse();
    }


    // ==================== existsByEmailEqualsIgnoreCaseAndIdNot ====================

    @Test
    void existsByEmailEqualsIgnoreCaseAndIdNot_ShouldReturnTrue_WhenAnotherBranchHasSameEmail() {

        Branch first = createAndSaveBranch();

        Branch second = createBranch(
                "Colombo",
                "CLM0001",
                "Colombo",
                "0112345678",
                "clm@sltb.lk"
        );

        second = branchRepository.save(second);

        boolean result =
                branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(
                        first.getEmail(),
                        second.getId()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmailEqualsIgnoreCaseAndIdNot_ShouldReturnFalse_WhenSameBranchIdIsExcluded() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(
                        branch.getEmail(),
                        branch.getId()
                );

        assertThat(result).isFalse();
    }


    // ==================== existsByTelephone ====================

    @Test
    void existsByTelephone_ShouldReturnTrue_WhenTelephoneExists() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByTelephone(
                        branch.getTelephone()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByTelephone_ShouldReturnFalse_WhenTelephoneDoesNotExist() {

        boolean result =
                branchRepository.existsByTelephone("0119999999");

        assertThat(result).isFalse();
    }


    // ==================== existsByTelephoneAndIdNot ====================

    @Test
    void existsByTelephoneAndIdNot_ShouldReturnTrue_WhenAnotherBranchHasSameTelephone() {

        Branch first = createAndSaveBranch();

        Branch second = createBranch(
                "Colombo",
                "CLM0001",
                "Colombo",
                "0112345678",
                "clm@sltb.lk"
        );

        second = branchRepository.save(second);

        boolean result =
                branchRepository.existsByTelephoneAndIdNot(
                        first.getTelephone(),
                        second.getId()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByTelephoneAndIdNot_ShouldReturnFalse_WhenSameBranchIdIsExcluded() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByTelephoneAndIdNot(
                        branch.getTelephone(),
                        branch.getId()
                );

        assertThat(result).isFalse();
    }


    // ==================== existsByAddressEqualsIgnoreCase ====================

    @Test
    void existsByAddressEqualsIgnoreCase_ShouldReturnTrue_WhenAddressExists() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByAddressEqualsIgnoreCase(
                        branch.getAddress().toLowerCase()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByAddressEqualsIgnoreCase_ShouldReturnFalse_WhenAddressDoesNotExist() {

        boolean result =
                branchRepository.existsByAddressEqualsIgnoreCase(
                        "Unknown Address"
                );

        assertThat(result).isFalse();
    }


    // ==================== existsByAddressEqualsIgnoreCaseAndIdNot ====================

    @Test
    void existsByAddressEqualsIgnoreCaseAndIdNot_ShouldReturnTrue_WhenAnotherBranchHasSameAddress() {

        Branch first = createAndSaveBranch();

        Branch second = createBranch(
                "Colombo",
                "CLM0001",
                "Colombo",
                "0112345678",
                "clm@sltb.lk"
        );

        second = branchRepository.save(second);

        boolean result =
                branchRepository.existsByAddressEqualsIgnoreCaseAndIdNot(
                        first.getAddress(),
                        second.getId()
                );

        assertThat(result).isTrue();
    }

    @Test
    void existsByAddressEqualsIgnoreCaseAndIdNot_ShouldReturnFalse_WhenSameBranchIdIsExcluded() {

        Branch branch = createAndSaveBranch();

        boolean result =
                branchRepository.existsByAddressEqualsIgnoreCaseAndIdNot(
                        branch.getAddress(),
                        branch.getId()
                );

        assertThat(result).isFalse();
    }


    // ==================== removeAll ====================

    @Test
    void removeAll_ShouldMarkBranchesAsDeleted() {

        Branch first = createAndSaveBranch();

        Branch second = createBranch(
                "Colombo",
                "CLM0001",
                "Colombo",
                "0112345678",
                "clm@sltb.lk"
        );

        second = branchRepository.save(second);

        branchRepository.removeAll(
                List.of(first.getId(), second.getId())
        );

        branchRepository.flush();

        Branch updatedFirst =
                branchRepository.findById(first.getId()).orElseThrow();

        Branch updatedSecond =
                branchRepository.findById(second.getId()).orElseThrow();

        assertThat(updatedFirst.isDeleted()).isTrue();
        assertThat(updatedSecond.isDeleted()).isTrue();
    }


    // ==================== Test Data ====================

    private Branch createAndSaveBranch() {

        Branch branch = createBranch(
                "Dambulla",
                "DML0001",
                "Kandy Road, Dambulla",
                "0665714150",
                "dbl@sltb.lk"
        );

        return branchRepository.save(branch);
    }

    private Branch createBranch(
            String name,
            String code,
            String address,
            String telephone,
            String email
    ) {

        BranchType branchType =
                branchTypeRepository.save(
                        BranchType.builder()
                                .name("Depot")
                                .build()
                );

        BranchStatus branchStatus =
                branchStatusRepository.save(
                        BranchStatus.builder()
                                .name("Active")
                                .build()
                );

        RegionalOffice regionalOffice =
                regionalOfficeRepository.save(
                        RegionalOffice.builder()
                                .name("Central Regional Office")
                                .build()
                );

        return Branch.builder()
                .name(name)
                .code(code)
                .address(address)
                .telephone(telephone)
                .email(email)
                .docreated(LocalDate.now().minusDays(100))
                .remarks("Repository test")
                .branchtype(branchType)
                .branchstatus(branchStatus)
                .regionaloffice(regionalOffice)
                .build();
    }
}
