package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Modifying
    @Transactional
    @Query("UPDATE Branch b SET b.deleted = false WHERE b.id IN :ids")
    void restoreAll(@Param("ids") List<Integer> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Branch  b SET b.deleted=true WHERE b.id in :ids")
    void removeAll(@Param("ids")List<Integer>ids);

}
