package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RosterRepository extends JpaRepository<Roster, Integer> {

    boolean existsByBranch_IdAndDoroster(Integer branchId, LocalDate doRoster);

    List<Roster> findByBranch_IdAndDorosterAndRosterstatus_Name(
            Integer branchId,
            LocalDate doroster,
            String rosterStatusName
    );

}
