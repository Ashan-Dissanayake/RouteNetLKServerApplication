package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RosterAssignmentRepository extends JpaRepository<Rosterassignement, Integer> {
    @Modifying
    @Query("""
    UPDATE Rosterassignement ra
       SET ra.rosterassignementstatus.id =
           (SELECT rs.id FROM Rosterassignementstatus rs WHERE rs.name = :status)
     WHERE ra.roster.id = :rosterId
""")
    void updateStatusByRosterId(
            @Param("rosterId") Integer rosterId,
            @Param("status") String status
    );

}
