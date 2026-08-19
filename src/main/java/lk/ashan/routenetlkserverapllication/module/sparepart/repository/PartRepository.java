package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for managing `Part` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {

    /**
     * Checks if a part exists by the given branch ID and part master ID.
     *
     * @param id the ID of the branch
     * @param id1 the ID of the part master
     * @return true if a part exists with the specified branch ID and part master ID, false otherwise
     */
    boolean existsByBranch_IdAndPartmaster_Id(Integer id, Integer id1);
}
