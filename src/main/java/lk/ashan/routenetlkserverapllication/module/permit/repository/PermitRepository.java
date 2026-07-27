package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PermitRepository extends JpaRepository<Permite, Integer> {
    boolean existsByNumber(String number);
    boolean existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(Integer id, Integer id1, Integer activeStatusId);

    List<Permite> findByPermitestatus_NameAndDoexpiredBefore(String name, LocalDate date);
    List<Permite> findByPermitestatus_NameAndDoexpiredBetween(String name, LocalDate startDate, LocalDate endDate);

}
