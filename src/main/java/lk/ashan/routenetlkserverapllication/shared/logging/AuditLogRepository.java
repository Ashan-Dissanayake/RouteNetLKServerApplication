package lk.ashan.routenetlkserverapllication.shared.logging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for persisting and querying {@link AuditLog} records.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    /** All audit events for a specific user (most-recent first). */
    List<AuditLog> findByUsernameOrderByCreatedAtDesc(String username);

    /** All audit events for a specific action type (paginated). */
    Page<AuditLog> findByActionOrderByCreatedAtDesc(String action, Pageable pageable);

    /** All audit events that occurred after a given timestamp. */
    List<AuditLog> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime after);

    /** All failed audit events (success = false). */
    List<AuditLog> findBySuccessFalseOrderByCreatedAtDesc();
}
