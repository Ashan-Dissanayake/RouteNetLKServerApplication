package lk.ashan.routenetlkserverapllication.shared.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service responsible for recording audit events.
 *
 * <p>All public methods are executed asynchronously so that audit persistence
 * never blocks the calling thread or slows down an HTTP response.</p>
 *
 * <h3>Typical usage inside a service or filter:</h3>
 * <pre>{@code
 * auditLogService.logSecurityEvent(
 *         "john.doe",
 *         AuditAction.LOGIN_SUCCESS,
 *         "Login from IP 192.168.1.1",
 *         request,
 *         true
 * );
 * }</pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // -------------------------------------------------------------------------
    // Security events
    // -------------------------------------------------------------------------

    /**
     * Records a security-related event (login, logout, access denial, etc.).
     *
     * @param username    authenticated username, or {@code "anonymous"}
     * @param action      event category (use constants from {@link AuditAction})
     * @param description human-readable summary of the event
     * @param request     the originating HTTP request (may be {@code null})
     * @param success     {@code true} if the security operation succeeded
     */
    @Async
    public void logSecurityEvent(
            String username,
            String action,
            String description,
            HttpServletRequest request,
            boolean success
    ) {
        persist(username, action, null, null, description, request, success);
        log.info("AUDIT [{}] user={} success={} | {}", action, username, success, description);
    }

    // -------------------------------------------------------------------------
    // Business / data-mutation events
    // -------------------------------------------------------------------------

    /**
     * Records a business data event (create, update, delete, export, etc.).
     *
     * @param username     authenticated username
     * @param action       event category (use constants from {@link AuditAction})
     * @param resourceType entity class name or domain label (e.g. {@code "Vehicle"})
     * @param resourceId   primary key / document number of the affected record
     * @param description  human-readable summary
     * @param request      the originating HTTP request (may be {@code null})
     * @param success      {@code true} if the operation succeeded
     */
    @Async
    public void logDataEvent(
            String username,
            String action,
            String resourceType,
            String resourceId,
            String description,
            HttpServletRequest request,
            boolean success
    ) {
        persist(username, action, resourceType, resourceId, description, request, success);
        log.info("AUDIT [{}] user={} resource={}/{} success={} | {}",
                action, username, resourceType, resourceId, success, description);
    }

    // -------------------------------------------------------------------------
    // Internal helper
    // -------------------------------------------------------------------------

    private void persist(
            String username,
            String action,
            String resourceType,
            String resourceId,
            String description,
            HttpServletRequest request,
            boolean success
    ) {
        try {
            AuditLog entry = AuditLog.builder()
                    .username(username != null ? username : "anonymous")
                    .action(action)
                    .resourceType(resourceType)
                    .resourceId(resourceId)
                    .description(description)
                    .ipAddress(request != null ? extractIp(request) : null)
                    .httpMethod(request != null ? request.getMethod() : null)
                    .requestUri(request != null ? request.getRequestURI() : null)
                    .success(success)
                    .build();

            auditLogRepository.save(entry);

        } catch (Exception ex) {
            // Audit persistence must NEVER crash the application
            log.error("Failed to persist audit log entry [action={}]: {}", action, ex.getMessage(), ex);
        }
    }

    private String extractIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
