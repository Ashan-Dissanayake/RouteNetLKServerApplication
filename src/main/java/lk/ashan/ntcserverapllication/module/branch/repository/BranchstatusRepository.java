package lk.ashan.ntcserverapllication.module.branch.repository;

import lk.ashan.ntcserverapllication.module.branch.model.Branchstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchstatusRepository extends JpaRepository<Branchstatus, Integer> {
}
