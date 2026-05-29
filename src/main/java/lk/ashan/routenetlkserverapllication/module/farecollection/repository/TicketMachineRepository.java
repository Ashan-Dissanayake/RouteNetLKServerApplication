package lk.ashan.routenetlkserverapllication.module.farecollection.repository;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketMachineRepository extends JpaRepository<TicketMachine, Integer> {
    Optional<TicketMachine> findByName(String name);
}
