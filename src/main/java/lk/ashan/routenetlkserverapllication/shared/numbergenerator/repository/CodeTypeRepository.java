package lk.ashan.routenetlkserverapllication.shared.numbergenerator.repository;

import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.CodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeTypeRepository extends JpaRepository<CodeType, Integer> {
    Optional<CodeType> findByName(String codeTypeName);
}
