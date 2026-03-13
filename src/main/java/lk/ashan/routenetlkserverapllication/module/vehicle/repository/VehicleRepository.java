package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("UPDATE Vehicle  v SET v.deleted=false WHERE v.id in :ids")
    void restoreAll(@Param("ids") List<Integer> ids);

    Optional<Vehicle> findByNumber(String number);


    List<Vehicle> findByBranch_IdAndDeletedFalse(Integer id);

    List<Vehicle> findByVehiclestatus_NameIn(Set<String> eligibleStatuses);

    List<Vehicle> findByVehiclestatus_Name(String statusName);
}
