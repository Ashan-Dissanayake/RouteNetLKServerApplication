package lk.ashan.routenetlkserverapllication.module.farecollection.repository;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `TicketMachine` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface TicketMachineRepository extends JpaRepository<TicketMachine, Integer> {

    /**
     * Finds a `TicketMachine` entity by its name.
     *
     * @param name the name of the ticket machine to search for
     * @return an `Optional` containing the found `TicketMachine`, or empty if not found
     */
    Optional<TicketMachine> findByName(String name);
}
