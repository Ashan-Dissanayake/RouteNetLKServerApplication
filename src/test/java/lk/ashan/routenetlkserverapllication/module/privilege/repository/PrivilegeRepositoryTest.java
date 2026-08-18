package lk.ashan.routenetlkserverapllication.module.privilege.repository;

import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Privilege;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/privilege-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class PrivilegeRepositoryTest extends BaseTestContainer {

    @Autowired
    private PrivilegeRepository privilegeRepository;


    // ========================================================
    // findByRoleId()
    // ========================================================

    @Test
    void findByRoleId_shouldReturnPrivilegesBelongingToRole() {

        List<Privilege> privileges =
                privilegeRepository.findByRoleId(9001);

        assertThat(privileges)
                .extracting(Privilege::getId)
                .containsExactlyInAnyOrder(
                        9001,
                        9002,
                        9003
                );
    }


    @Test
    void findByRoleId_shouldNotReturnPrivilegesBelongingToAnotherRole() {

        List<Privilege> privileges =
                privilegeRepository.findByRoleId(9001);

        assertThat(privileges)
                .extracting(Privilege::getId)
                .doesNotContain(9004);
    }


    @Test
    void findByRoleId_shouldReturnEmptyList_whenRoleHasNoPrivileges() {

        List<Privilege> privileges =
                privilegeRepository.findByRoleId(9999);

        assertThat(privileges).isEmpty();
    }


    // ========================================================
    // existsByRoleIdAndModuleIdAndOperationId()
    // ========================================================

    @Test
    void existsByRoleIdAndModuleIdAndOperationId_shouldReturnTrue_whenPrivilegeExists() {

        boolean result =
                privilegeRepository
                        .existsByRoleIdAndModuleIdAndOperationId(
                                9001,
                                9001,
                                9001
                        );

        assertThat(result).isTrue();
    }


    @Test
    void existsByRoleIdAndModuleIdAndOperationId_shouldReturnFalse_whenRoleDoesNotMatch() {

        boolean result =
                privilegeRepository
                        .existsByRoleIdAndModuleIdAndOperationId(
                                9002,
                                9001,
                                9001
                        );

        assertThat(result).isTrue();
    }


    @Test
    void existsByRoleIdAndModuleIdAndOperationId_shouldReturnFalse_whenModuleDoesNotMatch() {

        boolean result =
                privilegeRepository
                        .existsByRoleIdAndModuleIdAndOperationId(
                                9001,
                                9999,
                                9001
                        );

        assertThat(result).isFalse();
    }


    @Test
    void existsByRoleIdAndModuleIdAndOperationId_shouldReturnFalse_whenOperationDoesNotMatch() {

        boolean result =
                privilegeRepository
                        .existsByRoleIdAndModuleIdAndOperationId(
                                9001,
                                9001,
                                9999
                        );

        assertThat(result).isFalse();
    }


    @Test
    void existsByRoleIdAndModuleIdAndOperationId_shouldReturnFalse_whenCombinationDoesNotExist() {

        boolean result =
                privilegeRepository
                        .existsByRoleIdAndModuleIdAndOperationId(
                                9001,
                                9002,
                                9001
                        );

        assertThat(result).isFalse();
    }


    // ========================================================
    // existsByRoleIdAndId()
    // ========================================================

    @Test
    void existsByRoleIdAndId_shouldReturnTrue_whenPrivilegeBelongsToRole() {

        boolean result =
                privilegeRepository.existsByRoleIdAndId(
                        9001,
                        9001
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByRoleIdAndId_shouldReturnFalse_whenPrivilegeBelongsToAnotherRole() {

        boolean result =
                privilegeRepository.existsByRoleIdAndId(
                        9002,
                        9001
                );

        assertThat(result).isFalse();
    }


    @Test
    void existsByRoleIdAndId_shouldReturnFalse_whenPrivilegeDoesNotExist() {

        boolean result =
                privilegeRepository.existsByRoleIdAndId(
                        9001,
                        9999
                );

        assertThat(result).isFalse();
    }


    // ========================================================
    // deleteByRoleIdAndId()
    // ========================================================

    @Test
    void deleteByRoleIdAndId_shouldDeletePrivilegeBelongingToRole() {

        assertThat(
                privilegeRepository.existsByRoleIdAndId(9001, 9001)
        ).isTrue();

        privilegeRepository.deleteByRoleIdAndId(9001, 9001);

        assertThat(
                privilegeRepository.existsByRoleIdAndId(9001, 9001)
        ).isFalse();
    }


    @Test
    void deleteByRoleIdAndId_shouldNotDeletePrivilegeBelongingToAnotherRole() {

        privilegeRepository.deleteByRoleIdAndId(9002, 9001);

        assertThat(
                privilegeRepository.existsByRoleIdAndId(9001, 9001)
        ).isTrue();
    }
}
