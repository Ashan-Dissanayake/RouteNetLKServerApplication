package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Part  b SET b.deleted=true WHERE b.id in :ids")
    void removeAll(@Param("ids") List<Integer> ids);

    boolean existsByBranch_IdAndPartmaster_Id(Integer id, Integer id1);
}
