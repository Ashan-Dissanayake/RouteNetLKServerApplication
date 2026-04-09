package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnPartRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GrnPartRequestItemRepository extends JpaRepository<GrnPartRequestItem, Integer> {
    @Query("SELECT SUM(gp.quantity) FROM GrnPartRequestItem gp " +
            "WHERE gp.partrequestitem.id = :id " +
            "AND gp.grn.grnstatus.name IN :statusNames")
    BigDecimal sumQuantityByPartRequestItemId(
            @Param("id") Integer id,
            @Param("statusNames") List<String> statusNames
    );
}
