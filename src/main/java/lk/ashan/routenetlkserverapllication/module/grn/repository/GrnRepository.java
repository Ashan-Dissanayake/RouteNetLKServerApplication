package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrnRepository extends JpaRepository<Grn, Integer> {
    List<Grn> findByPartrequestIdAndGrnstatusNameIn(Integer id, List<String> received);

    //for testing
    List<Grn> findByGrnstatus_Name(String draft);
}
