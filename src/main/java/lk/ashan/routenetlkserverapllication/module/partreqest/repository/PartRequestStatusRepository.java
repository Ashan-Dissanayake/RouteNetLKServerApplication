package lk.ashan.routenetlkserverapllication.module.partreqest.repository;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.Partrequeststatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartRequestStatusRepository extends JpaRepository<Partrequeststatus, Integer> {
    Optional<Partrequeststatus> findByName(String pending);
}
