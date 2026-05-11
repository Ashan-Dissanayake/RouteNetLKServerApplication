package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Integer> {

    boolean existsByNumber(String number);
    boolean existsByNumberAndIdNot(String number, Integer id);

    List<Conductor> findByEmployee_Branch_Id(Integer branchId);
}
