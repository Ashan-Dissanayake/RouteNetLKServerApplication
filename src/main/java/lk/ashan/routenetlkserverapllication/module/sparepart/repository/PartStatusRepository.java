package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.Partstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartStatusRepository extends JpaRepository<Partstatus, Integer> {
    Optional<Partstatus> findByName(String name);
}
