package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RosterRepository extends JpaRepository<Roster, Integer> {

    boolean existsByBranch_IdAndDoroster(Integer branchId, LocalDate doRoster);
}
