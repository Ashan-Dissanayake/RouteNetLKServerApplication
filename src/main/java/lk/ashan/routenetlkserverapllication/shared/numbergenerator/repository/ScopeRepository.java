package lk.ashan.routenetlkserverapllication.shared.numbergenerator.repository;

import lk.ashan.routenetlkserverapllication.shared.numbergenerator.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, Integer> {
    Optional<Scope> findByName(String branchCode);
}
