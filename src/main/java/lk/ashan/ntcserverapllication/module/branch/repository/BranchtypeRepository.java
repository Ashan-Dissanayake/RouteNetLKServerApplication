package lk.ashan.ntcserverapllication.module.branch.repository;

import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchtypeRepository extends JpaRepository<Branchtype, Integer> {
}
