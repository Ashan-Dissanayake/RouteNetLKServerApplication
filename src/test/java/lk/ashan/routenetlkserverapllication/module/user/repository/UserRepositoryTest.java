package lk.ashan.routenetlkserverapllication.module.user.repository;


import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.shared.config.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql(
        scripts = "/sql/user-repository-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class UserRepositoryTest extends BaseTestContainer {

    @Autowired
    private UserRepository userRepository;


    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {

        Optional<User> result =
                userRepository.findByUsername("usertest9001");

        assertThat(result)
                .isPresent()
                .get()
                .extracting(User::getId)
                .isEqualTo(9001);
    }


    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {

        Optional<User> result =
                userRepository.findByUsername("nonexistent-user");

        assertThat(result).isEmpty();
    }


    @Test
    void findUserAccountLockedByUsername_shouldReturnTrue_whenAccountIsLocked() {

        Boolean result =
                userRepository.findUserAccountLockedByUsername("usertest9002");

        assertThat(result).isTrue();
    }


    @Test
    void findUserAccountLockedByUsername_shouldReturnFalse_whenAccountIsNotLocked() {

        Boolean result =
                userRepository.findUserAccountLockedByUsername("usertest9001");

        assertThat(result).isFalse();
    }


    @Test
    void existsByUsernameAndIdNot_shouldReturnTrue_whenAnotherUserHasSameUsername() {

        boolean result =
                userRepository.existsByUsernameAndIdNot(
                        "duplicate-user",
                        9001
                );

        assertThat(result).isTrue();
    }


    @Test
    void existsByUsernameAndIdNot_shouldReturnFalse_whenUsernameBelongsToSameUser() {

        boolean result =
                userRepository.existsByUsernameAndIdNot(
                        "usertest9001",
                        9001
                );

        assertThat(result).isFalse();
    }


    @Test
    void existsByUsernameAndIdNot_shouldReturnFalse_whenUsernameDoesNotExist() {

        boolean result =
                userRepository.existsByUsernameAndIdNot(
                        "nonexistent-user",
                        9001
                );

        assertThat(result).isFalse();
    }


    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {

        boolean result =
                userRepository.existsByUsername("usertest9001");

        assertThat(result).isTrue();
    }


    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotExist() {

        boolean result =
                userRepository.existsByUsername("nonexistent-user");

        assertThat(result).isFalse();
    }


    @Test
    void existsByEmployeeId_shouldReturnTrue_whenEmployeeHasUser() {

        boolean result =
                userRepository.existsByEmployee_Id(9001);

        assertThat(result).isTrue();
    }


    @Test
    void existsByEmployeeId_shouldReturnFalse_whenEmployeeHasNoUser() {

        boolean result =
                userRepository.existsByEmployee_Id(9003);

        assertThat(result).isFalse();
    }

    @Test
    void findByEmployeeBranchId_shouldReturnEmpty_whenBranchHasNoUsers() {

        List<User> users =
                userRepository.findByEmployee_Branch_Id(9002);

        assertThat(users).isEmpty();
    }
}
