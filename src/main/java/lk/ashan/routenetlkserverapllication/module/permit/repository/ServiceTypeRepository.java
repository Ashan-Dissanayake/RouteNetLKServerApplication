package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `ServiceType` entities.
 * Extends the `JpaRepository` to provide CRUD operations and custom queries.
 */
@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {

    /**
     * Finds a `ServiceType` entity by its name.
     *
     * @param number the name of the service type to search for
     * @return an `Optional` containing the found `ServiceType`, or empty if not found
     */
    Optional<ServiceType> findByName(String number);
}
