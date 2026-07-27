package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Opcalender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `Opcalender` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface OpCalenderRepository extends JpaRepository<Opcalender, Integer> {

    /**
     * Finds an `Opcalender` entity by its name.
     *
     * @param name the name of the `Opcalender` entity to find
     * @return an `Optional` containing the found `Opcalender` entity, or empty if not found
     */
    Optional<Opcalender> findByName(String name);
}
