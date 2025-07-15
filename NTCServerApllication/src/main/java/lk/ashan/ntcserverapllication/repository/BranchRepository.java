package lk.ashan.ntcserverapllication.repository;

import lk.ashan.ntcserverapllication.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
}
