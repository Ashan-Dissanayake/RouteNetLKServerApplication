package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    boolean existsByCodeEqualsIgnoreCase(String code);

    boolean existsByNameEqualsIgnoreCase(String name);
    boolean existsByNameEqualsIgnoreCaseAndIdNot(String name, Integer id);

    boolean existsByEmailEqualsIgnoreCase(String email);
    boolean existsByEmailEqualsIgnoreCaseAndIdNot(String email, Integer id);

    boolean existsByTelephone(String telephone);
    boolean existsByTelephoneAndIdNot(String telephone, Integer id);

    boolean existsByAddressEqualsIgnoreCase(String address);
    boolean existsByAddressEqualsIgnoreCaseAndIdNot(String address, Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE Branch  b SET b.deleted=true WHERE b.id in :ids")
    void removeAll(@Param("ids")List<Integer>ids);

}
