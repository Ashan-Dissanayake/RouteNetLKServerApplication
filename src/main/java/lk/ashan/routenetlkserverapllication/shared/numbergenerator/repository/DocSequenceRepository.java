package lk.ashan.routenetlkserverapllication.shared.numbergenerator.repository;

import jakarta.persistence.LockModeType;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.CodeType;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.DocSequence;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocSequenceRepository extends JpaRepository<DocSequence, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM DocSequence s WHERE s.codetype.id = :codeTypeId AND s.scope.id = :scopeId AND s.periodkey = :periodKey")
    Optional<DocSequence> findForUpdate(
            @Param("codeTypeId") Integer codeTypeId,
            @Param("scopeId") Integer scopeId,
            @Param("periodKey") String periodKey
    );

}
