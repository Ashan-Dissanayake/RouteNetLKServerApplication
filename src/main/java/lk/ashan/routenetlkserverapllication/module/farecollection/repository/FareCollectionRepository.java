package lk.ashan.routenetlkserverapllication.module.farecollection.repository;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report2Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FareCollectionRepository extends JpaRepository<FareCollection, Integer> {
    boolean existsByTripexecution_Id(Integer tripExecutionId);

    // --- REPORT 2: Depot-Wise Financial Reconciliation ---
    @Query(value = "SELECT b.name as depotName, " +
            "COALESCE(SUM(f.cashcollected), 0.0) as cashAmount, " +
            "COALESCE(SUM(f.digitalpayments), 0.0) as digitalAmount " +
            "FROM farecollection f " +
            "JOIN branch b ON f.branch_id = b.id " +
            "GROUP BY b.id, b.name", nativeQuery = true)
    List<Report2Projection> getDepotFinancialReconciliationMetrics();
}
