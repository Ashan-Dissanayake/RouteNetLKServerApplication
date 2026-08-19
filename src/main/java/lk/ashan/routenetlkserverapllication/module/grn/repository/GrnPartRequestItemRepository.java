package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnPartRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for managing `GrnPartRequestItem` entities.
 * Extends `JpaRepository` to provide CRUD operations and custom queries.
 */
@Repository
public interface GrnPartRequestItemRepository extends JpaRepository<GrnPartRequestItem, Integer> {

    /**
     * Calculates the sum of quantities for a specific part request item ID
     * and a list of GRN status names.
     *
     * @param id the ID of the part request item
     * @param statusNames the list of GRN status names to filter by
     * @return the sum of quantities as a `BigDecimal`
     * @throws IllegalArgumentException if the query parameters are invalid
     */
    @Query("""
            SELECT SUM(gp.quantity)
            FROM GrnPartRequestItem gp
            WHERE gp.partrequestitem.id = :id
            AND gp.grn.grnstatus.name IN :statusNames
            """)
    BigDecimal sumQuantityByPartRequestItemId(
            @Param("id") Integer id,
            @Param("statusNames") List<String> statusNames
    );
}
