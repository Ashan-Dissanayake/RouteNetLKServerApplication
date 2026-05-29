package lk.ashan.routenetlkserverapllication.module.farecollection.repository;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareCollectionRepository extends JpaRepository<FareCollection, Integer> {
    boolean existsByTripexecution_Id(Integer tripExecutionId);
}
