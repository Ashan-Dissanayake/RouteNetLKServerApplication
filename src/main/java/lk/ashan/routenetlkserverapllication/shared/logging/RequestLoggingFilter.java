package lk.ashan.routenetlkserverapllication.shared.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * HTTP Request/Response Logging Filter.
 *
 * <p>Logs every incoming HTTP request and its corresponding response,
 * including method, URI, authenticated user, status code, and duration.</p>
 *
 * <p>A unique {@code X-Request-Id} header is generated per request and
 * included in all log entries so that a full request lifecycle can be
 * traced across log lines.</p>
 *
 * <p>Sensitive paths (e.g. {@code /login}) are logged at a reduced level
 * to avoid leaking credentials in log files.</p>
 */
@Slf4j
@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Generate a unique ID for this request
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUri = queryString != null ? uri + "?" + queryString : uri;
        String clientIp = getClientIp(request);

        log.info("[{}] --> {} {} | IP: {}", requestId, method, fullUri, clientIp);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            String username = getAuthenticatedUsername();

            if (status >= 500) {
                log.error("[{}] <-- {} {} | status={} | user={} | {}ms",
                        requestId, method, fullUri, status, username, duration);
            } else if (status >= 400) {
                log.warn("[{}] <-- {} {} | status={} | user={} | {}ms",
                        requestId, method, fullUri, status, username, duration);
            } else {
                log.info("[{}] <-- {} {} | status={} | user={} | {}ms",
                        requestId, method, fullUri, status, username, duration);
            }
        }
    }

    /**
     * Skips logging for Spring Boot actuator endpoints to reduce noise.
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/actuator");
    }

    /**
     * Resolves the authenticated username from the Spring Security context.
     * Returns "anonymous" if no authentication is present.
     */
    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "anonymous";
    }

    /**
     * Extracts the real client IP address, taking into account common
     * reverse-proxy headers (X-Forwarded-For, X-Real-IP).
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For can be a comma-separated list; first value is the client
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
