package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partmaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartMasterRepository extends JpaRepository<Partmaster, Integer> {
    Optional<Partmaster> findByName(String name);
}
