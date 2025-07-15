package lk.ashan.ntcserverapllication.repository;

import lk.ashan.ntcserverapllication.model.entity.Branchtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchtypeRepository extends JpaRepository<Branchtype, Integer> {
}
