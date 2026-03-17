package lk.ashan.routenetlkserverapllication.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Service for tracking and managing login attempts based on IP addresses.
 * This service monitors login failures and temporarily blocks IP addresses
 * after exceeding the maximum allowed failed attempts.
 *
 * Features:
 * - Automatic expiration of blocked IPs after configured duration
 * - Thread-safe concurrent access
 * - Configurable thresholds and timeouts
 * - Login success tracking to clear failed attempts
 */
@Service
@Slf4j
public class LoginAttemptService {

    /**
     * Maximum allowed failed login attempts before IP is blocked
     */
    @Value("${security.login.max-attempts:5}")
    private int maxAttempts;

    /**
     * Block duration in minutes after max attempts exceeded
     */
    @Value("${security.login.block-duration-minutes:15}")
    private int blockDurationMinutes;

    /**
     * Cache for tracking failed login attempts per IP address.
     * Entries automatically expire after the configured block duration.
     */
    private LoadingCache<String, Integer> attemptsCache;

    /**
     * Initializes the IP-based login attempts cache with configured settings.
     * This method is called automatically after dependency injection.
     */
    @PostConstruct
    public void init() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(blockDurationMinutes, TimeUnit.MINUTES)
                .maximumSize(1000) // Limit memory usage
                .recordStats() // Enable statistics for monitoring
                .build(CacheLoader.from(() -> 0)); // Default value is 0

        log.info("LoginAttemptService initialized: MAX_ATTEMPTS={}, BLOCK_DURATION={}min",
                maxAttempts, blockDurationMinutes);
    }

    /**
     * Records a failed login attempt for the specified IP address.
     * Increments the failure counter and logs warnings when approaching or reaching the limit.
     *
     * @param ip the IP address that failed to authenticate
     */
    public void loginFailed(String ip) {
        if (ip == null || ip.isEmpty()) {
            log.warn("Attempted to record failed login for null/empty IP");
            return;
        }

        try {
            int attempts = attemptsCache.get(ip);
            int newAttempts = attempts + 1;
            attemptsCache.put(ip, newAttempts);

            if (newAttempts >= maxAttempts) {
                log.warn("IP address {} has been BLOCKED after {} failed attempts (expires in {}min)",
                        ip, newAttempts, blockDurationMinutes);
            } else if (newAttempts >= maxAttempts - 1) {
                log.warn("IP address {} has {} failed attempts (1 more will trigger block)",
                        ip, newAttempts);
            } else {
                log.debug("Failed login from IP {}. Attempt #{}/{}", ip, newAttempts, maxAttempts);
            }
        } catch (ExecutionException e) {
            log.error("Error tracking failed login attempt for IP: {}", ip, e);
        }
    }

    /**
     * Records a successful login and clears any failed attempts for the IP.
     * This resets the counter, allowing the user to start fresh.
     *
     * @param ip the IP address that successfully authenticated
     */
    public void loginSucceeded(String ip) {
        if (ip == null || ip.isEmpty()) {
            return;
        }

        try {
            int previousAttempts = attemptsCache.get(ip);
            if (previousAttempts > 0) {
                attemptsCache.invalidate(ip);
                log.info("Cleared {} failed login attempts for IP {} after successful login",
                        previousAttempts, ip);
            }
        } catch (ExecutionException e) {
            log.error("Error clearing attempts for IP: {}", ip, e);
        }
    }

    /**
     * Checks if an IP address is currently blocked due to excessive failed attempts.
     *
     * @param ip the IP address to check
     * @return true if the IP is blocked (attempts >= maxAttempts), false otherwise
     */
    public boolean isBlocked(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        try {
            int attempts = attemptsCache.get(ip);
            boolean blocked = attempts >= maxAttempts;

            if (blocked) {
                log.debug("IP {} is currently BLOCKED (attempts: {})", ip, attempts);
            }

            return blocked;
        } catch (ExecutionException e) {
            log.error("Error checking if IP is blocked: {}", ip, e);
            return false; // Fail open - don't block on errors
        }
    }

    /**
     * Gets the current number of failed attempts for an IP address.
     * Useful for logging, monitoring, or showing warnings to users.
     *
     * @param ip the IP address to check
     * @return the number of failed attempts, or 0 if none
     */
    public int getAttempts(String ip) {
        if (ip == null || ip.isEmpty()) {
            return 0;
        }

        try {
            return attemptsCache.get(ip);
        } catch (ExecutionException e) {
            log.error("Error getting attempts for IP: {}", ip, e);
            return 0;
        }
    }

    /**
     * Gets the remaining attempts before the IP is blocked.
     *
     * @param ip the IP address to check
     * @return number of remaining attempts, or 0 if already blocked
     */
    public int getRemainingAttempts(String ip) {
        int current = getAttempts(ip);
        return Math.max(0, maxAttempts - current);
    }

    /**
     * Manually clears all failed attempts for an IP address.
     * Useful for administrative purposes or testing.
     *
     * @param ip the IP address to unblock
     */
    public void clearAttempts(String ip) {
        if (ip == null || ip.isEmpty()) {
            return;
        }

        attemptsCache.invalidate(ip);
        log.info("Manually cleared all attempts for IP: {}", ip);
    }

    /**
     * Gets cache statistics for monitoring.
     *
     * @return string representation of cache statistics
     */
    public String getCacheStats() {
        return attemptsCache.stats().toString();
    }

    /**
     * Gets the current number of IPs being tracked.
     *
     * @return the size of the cache
     */
    public long getTrackedIpCount() {
        return attemptsCache.size();
    }
}
