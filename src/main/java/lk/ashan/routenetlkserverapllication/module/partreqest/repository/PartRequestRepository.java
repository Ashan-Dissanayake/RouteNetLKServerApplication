package lk.ashan.routenetlkserverapllication.module.partreqest.repository;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing `PartRequest` entities.
 * Extends the `JpaRepository` to provide CRUD operations and custom queries.
 */
@Repository
public interface PartRequestRepository extends JpaRepository<PartRequest, Integer> {

    /**
     * Checks if a `PartRequest` exists for a specific branch, part, status, and request date.
     *
     * @param branchId    the ID of the branch to check
     * @param partId      the ID of the part to check
     * @param statusNames the list of status names to check
     * @param requestDate the date of the request to check
     * @return `true` if a matching `PartRequest` exists, otherwise `false`
     * @throws IllegalArgumentException if any parameter is invalid
     */
    @Query("""
        SELECT CASE WHEN COUNT(pri) > 0 THEN true ELSE false END
        FROM PartRequest pr
        JOIN pr.partrequestitems pri
        JOIN pr.partrequeststatus prs
        WHERE pr.branch.id = :branchId
          AND pri.part.id = :partId
          AND prs.name IN :statusNames
          AND pr.dorequested = :requestDate
        """)
    boolean existsByBranchAndPartAndStatusInAndDoRequested(
            @Param("branchId") Integer branchId,
            @Param("partId") Integer partId,
            @Param("statusNames") List<String> statusNames,
            @Param("requestDate") LocalDate requestDate
    );
}
