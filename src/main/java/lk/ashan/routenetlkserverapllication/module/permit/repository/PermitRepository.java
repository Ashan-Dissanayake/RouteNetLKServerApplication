package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitRepository extends JpaRepository<Permite, Integer> {
    boolean existsByNumber(String number);
    boolean existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(Integer id, Integer id1, Integer activeStatusId)
}
