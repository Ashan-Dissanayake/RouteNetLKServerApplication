package lk.ashan.ntcserverapllication.repository;

import lk.ashan.ntcserverapllication.model.entity.Branchstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchstatusRepository extends JpaRepository<Branchstatus, Integer> {
}
