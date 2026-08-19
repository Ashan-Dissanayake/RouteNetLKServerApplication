package lk.ashan.routenetlkserverapllication.module.farecollection.repository;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report2Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing FareCollection entities.
 * Provides methods for querying and interacting with the FareCollection database table.
 */
@Repository
public interface FareCollectionRepository extends JpaRepository<FareCollection, Integer> {

    /**
     * Checks if a FareCollection entry exists for the given trip execution ID.
     *
     * @param tripExecutionId the ID of the trip execution to check
     * @return true if an entry exists, false otherwise
     */
    boolean existsByTripexecution_Id(Integer tripExecutionId);

    /**
     * Retrieves depot-wise financial reconciliation metrics.
     * This includes the depot name, total cash collected, and total digital payments.
     *
     * @return a list of Report2Projection containing depot financial reconciliation metrics
     */
    @Query(value = "SELECT b.name as depotName, " +
            "COALESCE(SUM(f.cashcollected), 0.0) as cashAmount, " +
            "COALESCE(SUM(f.digitalpayments), 0.0) as digitalAmount " +
            "FROM farecollection f " +
            "JOIN branch b ON f.branch_id = b.id " +
            "GROUP BY b.id, b.name", nativeQuery = true)
    List<Report2Projection> getDepotFinancialReconciliationMetrics();

    /**
     * Retrieves a daily revenue summary for a specific branch.
     * The summary includes the total tickets, cash collected, and digital payments for the current date.
     *
     * @param branchId the ID of the branch for which the summary is retrieved
     * @return an Object array containing the total tickets, cash collected, and digital payments
     */
    @Query("SELECT COALESCE(SUM(f.totaltickets), 0), " +
            "       COALESCE(SUM(f.cashcollected), 0), " +
            "       COALESCE(SUM(f.digitalpayments), 0) " +
            "FROM FareCollection f " +
            "WHERE f.branch.id = :branchId " +
            "AND f.tripexecution.doservice = CURRENT_DATE")
    Object[] getDailyRevenueSummaryByBranch(@Param("branchId") Integer branchId);
}
