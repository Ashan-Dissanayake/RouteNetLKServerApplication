package lk.ashan.routenetlkserverapllication.security;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.ModuleRepository;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service implementation for handling user authentication and details retrieval
 * required by Spring Security. This class implements the {@link UserDetailsService}
 * interface, providing methods to authenticate users and retrieve user-specific
 * details, such as authorities and account state.
 * <p>
 * Dependencies:
 * - {@link ModuleRepository} is used to fetch system modules for determining administrative authorities.
 * - {@link UserRepository} is used to retrieve user details and account state from the database.
 * - {@link PasswordEncoder} is used to encode the in-memory admin password.
 * <p>
 * Configuration:
 * - The service supports an in-memory user with a username and password defined
 * via properties `spring.security.user.name` and `spring.security.user.password`.
 * <p>
 * Responsibilities:
 * - Load user details by username and generate a {@link UserDetails} object.
 * - Differentiate between in-memory administrative users and database-stored users.
 * - Provide built-in support for user account locking and privilege-based authority management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.user.name:admin}")
    private String inMemoryUserName;

    @Value("${spring.security.user.password:admin}")
    private String inMemoryUserPassword;

    /**
     * Loads the user details associated with the specified username.
     * <p>
     * This method retrieves a user by the given username and constructs a {@code UserDetails}
     * object containing the user's properties and granted authorities. If the username is not
     * found, a {@code UsernameNotFoundException} is thrown.
     *
     * @param username the username provided for locating the user
     * @return a {@code UserDetails} object containing the user's information and authorities
     * @throws UsernameNotFoundException if no user with the specified username is found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for username: {}", username);

        User foundUser = findUserByUsername(username);
        UserDetails userDetails = buildUserDetails(foundUser);

        log.debug("User '{}' loaded with {} authorities",
                username, userDetails.getAuthorities().size());

        return userDetails;
    }

    /**
     * Searches for a user entity based on the provided username. If the username matches
     * the preconfigured in-memory username, an in-memory user is created and returned.
     * Otherwise, a lookup is performed in the database to retrieve the user details.
     *
     * @param username the username of the user to be searched
     * @return the {@code User} entity corresponding to the provided username
     * @throws UsernameNotFoundException if no user is found with the provided username
     */
    private User findUserByUsername(String username) {
        if (username.equals(inMemoryUserName)) {
            log.debug("Loading in-memory admin user: {}", username);
            return createInMemoryUser();
        }

        log.debug("Looking up user in database: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found in database: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }

    /**
     * Builds a {@link UserDetails} object from the provided {@link User} entity.
     * This method configures the user's account settings (e.g., locked, expired)
     * and assigns roles or authorities based on their username and stored privileges.
     *
     * @param user the user entity containing necessary information such as username,
     *             password, and roles to be converted into a {@link UserDetails} object.
     * @return a {@link UserDetails} object configured with the user's data and authorities.
     */
    private UserDetails buildUserDetails(User user) {
        boolean isInMemoryUser = user.getUsername().equals(inMemoryUserName);
        boolean isLocked = isUserAccountLocked(user.getUsername());

        Set<SimpleGrantedAuthority> authorities = isInMemoryUser
                ? getAdminAuthorities()
                : getUserAuthorities(user);

        if (isLocked) {
            log.warn("User '{}' account is locked", user.getUsername());
        }

        log.debug("Building UserDetails for '{}': locked={}, authorities={}",
                user.getUsername(), isLocked, authorities.size());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(isLocked)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Creates and returns a new in-memory user object using the predefined
     * username and password configured in application properties.
     * <p>
     * IMPORTANT: The password is encoded using BCrypt for security.
     *
     * @return a {@link User} instance representing the in-memory user with the configured
     * username and BCrypt-encoded password.
     */
    private User createInMemoryUser() {
        // IMPORTANT: Encode the password for security
        String encodedPassword = passwordEncoder.encode(inMemoryUserPassword);

        return User.builder()
                .username(inMemoryUserName)
                .password(encodedPassword)
                .build();
    }

    /**
     * Generates a set of administrative authorities based on system modules and their standard operations.
     * The method retrieves a collection of modules and assigns a set of permissions for each module.
     * For the "user" module, an additional "lock" operation is appended to the standard operations.
     * Each authority is identified by a combination of the module name and operation in the format "moduleName-operation".
     *
     * @return a set of {@link SimpleGrantedAuthority} instances representing the administrative authorities for all modules.
     */
    protected Set<SimpleGrantedAuthority> getAdminAuthorities() {
        log.debug("Generating admin authorities");

        var standardOperations = Set.of("select", "insert", "update", "delete");

        // Define bootstrap modules to guarantee in-memory user can access everything without seeded DB
        var bootstrapModules = Set.of(
                "branch", "employee", "crew", "driver", "conductor", "vehicle", "permit",
                "part", "part-request", "grn", "roster", "trip", "trip-execution",
                "incident", "incident-vehicle-allocation", "fare-collection", "vehicle-service", "user"
        );

        // Map bootstrap modules to authorities
        Set<SimpleGrantedAuthority> authorities = bootstrapModules.stream()
                .flatMap(mod -> {
                    var operations = mod.equalsIgnoreCase("user")
                            ? Stream.concat(standardOperations.stream(), Stream.of("lock"))
                            : standardOperations.stream();
                    return operations.map(op -> new SimpleGrantedAuthority(mod + "-" + op));
                })
                .collect(Collectors.toSet());

        // Also query database modules if any exist
        try {
            moduleRepository.findAll().forEach(module -> {
                String modName = module.getName().toLowerCase();
                var operations = modName.equalsIgnoreCase("user")
                        ? Stream.concat(standardOperations.stream(), Stream.of("lock"))
                        : standardOperations.stream();
                operations.forEach(op -> authorities.add(new SimpleGrantedAuthority(modName + "-" + op)));
            });
        } catch (Exception e) {
            log.warn("Failed to retrieve modules from database: {}", e.getMessage());
        }

        // Grant the in-memory admin user the DEPOT_SUPERINTENDENT role
        authorities.add(new SimpleGrantedAuthority("ROLE_DEPOT_SUPERINTENDENT"));

        log.debug("Generated {} admin authorities (including DEPOT_SUPERINTENDENT role)", authorities.size());
        return authorities;
    }

    /**
     * Retrieves the authorities associated with a given user from the database.
     * The authorities are derived from the user's roles and their associated privileges.
     *
     * @param user The {@link User} entity containing roles and privileges information.
     * @return A set of {@link SimpleGrantedAuthority} instances representing the user's authorities.
     */
    private Set<SimpleGrantedAuthority> getUserAuthorities(User user) {
        if (user.getUserRoles() == null || user.getUserRoles().isEmpty()) {
            log.warn("User '{}' has no roles assigned", user.getUsername());
            return Set.of();
        }

        // 1. Retrieve granular privileges (authorities)
        Set<SimpleGrantedAuthority> authorities = user.getUserRoles().stream()
                .flatMap(userRole -> {
                    if (userRole.getRole() == null) {
                        log.warn("UserRole for user '{}' has null role", user.getUsername());
                        return Stream.empty();
                    }
                    if (userRole.getRole().getPrivileges() == null) {
                        log.warn("Role '{}' for user '{}' has no privileges",
                                userRole.getRole().getName(), user.getUsername());
                        return Stream.empty();
                    }
                    return userRole.getRole().getPrivileges().stream();
                })
                .filter(privilege -> privilege.getAuthority() != null)
                .map(privilege -> new SimpleGrantedAuthority(privilege.getAuthority()))
                .collect(Collectors.toSet());

        // 2. Retrieve role names and prefix with "ROLE_" for role-based access checks
        Set<SimpleGrantedAuthority> roleAuthorities = user.getUserRoles().stream()
                .filter(userRole -> userRole.getRole() != null && userRole.getRole().getName() != null)
                .map(userRole ->
                        new SimpleGrantedAuthority(
                                "ROLE_" +
                                        userRole.getRole()
                                                .getName()
                                                .trim()
                                                .toUpperCase()
                                                .replace(" ", "_")
                        )
                ).collect(Collectors.toSet());

        authorities.addAll(roleAuthorities);

        log.debug("User '{}' loaded with {} authorities (privileges + roles)", user.getUsername(), authorities.size());

        if (authorities.isEmpty()) {
            log.warn("User '{}' has no authorities after processing", user.getUsername());
        }

        return authorities;
    }

    /**
     * Determines whether the user account is locked based on the specified username.
     * This method differentiates between an in-memory user (always unlocked) and a
     * user fetched from the database. For database users, it queries the repository
     * to check the account locked status.
     *
     * @param userName the username of the account to be checked
     * @return {@code true} if the user account is locked; {@code false} otherwise
     */
    public boolean isUserAccountLocked(String userName) {
        if (userName == null || userName.isEmpty()) {
            return false;
        }

        if (userName.equalsIgnoreCase(inMemoryUserName)) {
            return false;
        }

        try {
            Boolean locked = userRepository.findUserAccountLockedByUsername(userName);
            return locked != null && locked;
        } catch (Exception e) {
            log.error("Error checking account lock status for user: {}", userName, e);
            return false; // Fail open - don't lock on errors
        }
    }
}
