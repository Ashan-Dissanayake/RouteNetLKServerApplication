package lk.ashan.routenetlkserverapllication.shared.logging;

/**
 * Catalogue of audit-action constants used when recording {@link AuditLog} entries.
 *
 * <p>Grouping constants here keeps action strings consistent across the codebase
 * and makes log queries predictable.</p>
 *
 * <h3>Security events</h3>
 * <ul>
 *   <li>{@link #LOGIN_SUCCESS}</li>
 *   <li>{@link #LOGIN_FAILURE}</li>
 *   <li>{@link #ACCOUNT_LOCKED}</li>
 *   <li>{@link #ACCOUNT_DISABLED}</li>
 *   <li>{@link #ACCESS_DENIED}</li>
 *   <li>{@link #TOKEN_EXPIRED}</li>
 *   <li>{@link #TOKEN_INVALID}</li>
 *   <li>{@link #LOGOUT}</li>
 * </ul>
 *
 * <h3>Data events</h3>
 * <ul>
 *   <li>{@link #CREATE}</li>
 *   <li>{@link #UPDATE}</li>
 *   <li>{@link #DELETE}</li>
 *   <li>{@link #EXPORT}</li>
 * </ul>
 */
public final class AuditAction {

    private AuditAction() { /* utility class — no instances */ }

    // ------------------------------------------------------------------
    // Security events
    // ------------------------------------------------------------------

    /** User authenticated successfully. */
    public static final String LOGIN_SUCCESS    = "LOGIN_SUCCESS";

    /** Authentication attempt failed (bad credentials, user not found, etc.). */
    public static final String LOGIN_FAILURE    = "LOGIN_FAILURE";

    /** Account locked after too many failed attempts. */
    public static final String ACCOUNT_LOCKED   = "ACCOUNT_LOCKED";

    /** Authentication rejected because the account is disabled. */
    public static final String ACCOUNT_DISABLED = "ACCOUNT_DISABLED";

    /** Request rejected due to insufficient permissions. */
    public static final String ACCESS_DENIED    = "ACCESS_DENIED";

    /** Request rejected because the JWT has expired. */
    public static final String TOKEN_EXPIRED    = "TOKEN_EXPIRED";

    /** Request rejected because the JWT is malformed or has an invalid signature. */
    public static final String TOKEN_INVALID    = "TOKEN_INVALID";

    /** User logged out explicitly. */
    public static final String LOGOUT           = "LOGOUT";

    // ------------------------------------------------------------------
    // Data events
    // ------------------------------------------------------------------

    /** A new resource was created. */
    public static final String CREATE = "CREATE";

    /** An existing resource was updated. */
    public static final String UPDATE = "UPDATE";

    /** A resource was soft-deleted or permanently removed. */
    public static final String DELETE = "DELETE";

    /** Data was exported (report, CSV, etc.). */
    public static final String EXPORT = "EXPORT";
}
