package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report5Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    boolean existsByNumber(String number);

    @Query("select v from Vehicle v where v.id=:id")
    Vehicle findByMyId(@Param("id")Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE Vehicle  v SET v.deleted=true WHERE v.id in :ids")
    void removeAll(@Param("ids") List<Integer> ids);

    List<Vehicle> findByBranch_Id(Integer branchId);
}
