package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Integer id);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByTelephone(String telephone);
    boolean existsByTelephoneAndIdNot(String telephone, Integer id);


}
