package lk.ashan.routenetlkserverapllication.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Custom AuthenticationProvider implementation for providing authentication logic
 * using username and password with added security measures such as login attempt tracking
 * and account lock features.
 *
 * This class works with {@link MyUserDetailsService} to load user details,
 * validates login attempts for brute-force prevention, and handles account-level restrictions.
 *
 * The class uses a cache to track login attempts and prevents authentication
 * after a configurable number of failed attempts until the lockout period expires.
 *
 * Components and services collaborating with this class:
 * - {@link MyUserDetailsService}: Loads user details from the database or in-memory storage.
 * - {@link LoginAttemptService}: Tracks and blocks login attempts based on IP address.
 * - {@link PasswordEncoder}: Encodes and validates password hashes during authentication.
 *
 * Major features:
 * - Support for both in-memory and database-stored users.
 * - Login attempt tracking and temporary account lock support.
 * - Block IP addresses after exceeding failed-login thresholds.
 * - Password validation using a configurable {@link PasswordEncoder}.
 * - Account lock detection and administration.
 *
 * Notes on Configuration:
 * - `MAX_ATTEMPTS` specifies the maximum number of allowed failed login attempts per username.
 * - `CACHE_EXPIRY_SECONDS` defines the duration in seconds before the failed login attempt cache is reset.
 *
 * Thread Safety:
 * The class uses thread-safe constructs like Google's LoadingCache
 * and {@link LoginAttemptService} to manage concurrent login attempts and blocking operations.
 *
 * Exceptions Thrown:
 * - {@link BadCredentialsException}: Thrown when invalid credentials are provided.
 * - {@link UsernameNotFoundException}: Thrown when the specified user does not exist.
 * - {@link LockedException}: Raised for temporary or permanent account locks.
 * - {@link AuthenticationException}: Indicates a common authentication process failure.
 * - {@link AuthenticationServiceException}: Thrown when service or infrastructure fails during authentication.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    /**
     * Maximum allowed login attempts before temporary lockout
     */
    private static final int MAX_ATTEMPTS = 3;

    /**
     * Cache expiry duration in seconds for failed login attempts
     */
    private static final int CACHE_EXPIRY_SECONDS = 5;

    /**
     * Unknown IP address identifier when request context is unavailable
     */
    private static final String UNKNOWN_IP = "unknown";

    private final MyUserDetailsService myUserDetailsService;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cache for tracking failed authentication attempts per username
     */
    private LoadingCache<String, Integer> attemptsCache;

    /**
     * Initializes the attempts cache with configured expiration and size limits.
     * This method is called automatically after dependency injection is complete.
     */
    @PostConstruct
    public void init() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(CACHE_EXPIRY_SECONDS, TimeUnit.SECONDS)
                .maximumSize(10000) // Prevent memory leaks
                .recordStats() // Enable statistics for monitoring
                .build(CacheLoader.from(key -> 0));

        log.info("Authentication provider initialized with MAX_ATTEMPTS={}, CACHE_EXPIRY={}s",
                MAX_ATTEMPTS, CACHE_EXPIRY_SECONDS);
    }

    /**
     * Authenticates the provided authentication object. Validates the login attempts
     * based on the client's IP address and credentials. If the user exceeds the allowed login
     * attempts or provides invalid credentials, appropriate exceptions are thrown.
     *
     * @param authentication the authentication object containing username and credentials
     * @return an authenticated Authentication object if authentication succeeds
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = Optional.ofNullable(authentication.getCredentials())
                .map(Object::toString)
                .orElseThrow(() -> new BadCredentialsException("Password cannot be empty"));

        String clientIP = getClientIP();

        log.debug("Authentication attempt for username='{}' from IP={}", username, clientIP);

        try {
            validateLoginAttempts(clientIP, username);
            return performAuthentication(username, password, clientIP);
        } catch (ExecutionException e) {
            log.error("Authentication cache error for user: {}", username, e);
            loginAttemptService.loginFailed(clientIP);
            throw new AuthenticationServiceException("Authentication service temporarily unavailable", e);
        }
    }

    /**
     * Validates login attempts for a given client IP and username.
     * Checks if the IP is blocked or if the username has exceeded maximum attempts.
     *
     * @param clientIP the IP address of the client
     * @param username the username being authenticated
     * @throws ExecutionException if cache operation fails
     * @throws LockedException if IP is blocked or account has too many failed attempts
     */
    private void validateLoginAttempts(String clientIP, String username) throws ExecutionException {
        // Check IP-based blocking
        if (loginAttemptService.isBlocked(clientIP)) {
            log.warn("Blocked IP address attempted login: {}", clientIP);
            throw new LockedException(
                    "Too many failed login attempts from your network. Please try again later."
            );
        }

        // Check username-based attempts
        int attempts = attemptsCache.get(username);
        if (attempts >= MAX_ATTEMPTS) {
            log.warn("Account '{}' temporarily locked due to {} failed attempts", username, attempts);
            throw new LockedException(
                    String.format("Too many failed login attempts. Please try again in %d seconds.",
                            CACHE_EXPIRY_SECONDS)
            );
        }

        if (attempts > 0) {
            log.debug("Previous failed attempts for '{}': {}", username, attempts);
        }
    }

    /**
     * Performs the actual authentication by loading user details and validating credentials.
     *
     * @param username the username to authenticate
     * @param password the password to verify
     * @param clientIP the client's IP address
     * @return authenticated Authentication object
     * @throws AuthenticationException if authentication fails
     */
    private Authentication performAuthentication(String username, String password, String clientIP) {
        try {
            // Load user details
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            // Check for permanent account lock
            if (myUserDetailsService.isUserAccountLocked(username)) {
                log.warn("Attempt to access permanently locked account: {}", username);
                throw new LockedException(
                        "This account has been locked by an administrator. Please contact support."
                );
            }

            // Validate password
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                // Success - clear failed attempts
                attemptsCache.invalidate(username);
                loginAttemptService.loginSucceeded(clientIP);

                log.info("Successful authentication for user '{}' from IP {}", username, clientIP);

                return new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
            }

            // Password mismatch
            handleFailedAttempt(username, clientIP);
            throw new BadCredentialsException("Invalid username or password");

        } catch (UsernameNotFoundException e) {
            // User not found - treat same as bad credentials for security
            handleFailedAttempt(username, clientIP);
            log.debug("Authentication failed - user not found: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * Handles a failed login attempt by updating caches and counters.
     *
     * @param username the username that failed authentication
     * @param clientIP the IP address of the failed attempt
     */
    private void handleFailedAttempt(String username, String clientIP) {
        try {
            loginAttemptService.loginFailed(clientIP);
            int attempts = attemptsCache.get(username);
            int newAttempts = attempts + 1;
            attemptsCache.put(username, newAttempts);

            log.warn("Failed login attempt for user '{}' from IP {}. Attempt #{}",
                    username, clientIP, newAttempts);

            if (newAttempts >= MAX_ATTEMPTS) {
                log.warn("User '{}' has reached maximum attempts ({}). Temporarily locked.",
                        username, MAX_ATTEMPTS);
            }
        } catch (ExecutionException e) {
            log.error("Error updating failed attempts cache for user: {}", username, e);
        }
    }

    /**
     * Determines if this provider supports the given authentication type.
     *
     * @param authentication the authentication class to check
     * @return true if UsernamePasswordAuthenticationToken is supported
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * Retrieves the client's IP address from the current HTTP request.
     * Checks X-Forwarded-For header for proxy scenarios.
     *
     * @return the client's IP address, or "unknown" if unavailable
     */
    private String getClientIP() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                log.warn("No request context available for IP extraction");
                return UNKNOWN_IP;
            }

            HttpServletRequest request = attributes.getRequest();

            // Check X-Forwarded-For header (for proxied requests)
            String xfHeader = request.getHeader("X-Forwarded-For");
            if (xfHeader != null && !xfHeader.isEmpty()) {
                // Get first IP in the chain
                String ip = xfHeader.split(",")[0].trim();
                log.debug("Client IP from X-Forwarded-For: {}", ip);
                return ip;
            }

            // Fallback to remote address
            String ip = request.getRemoteAddr();
            log.debug("Client IP from remote address: {}", ip);
            return ip;

        } catch (Exception e) {
            log.error("Error extracting client IP address", e);
            return UNKNOWN_IP;
        }
    }
}
