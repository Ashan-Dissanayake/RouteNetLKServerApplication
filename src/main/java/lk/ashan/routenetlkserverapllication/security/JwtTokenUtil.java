package lk.ashan.routenetlkserverapllication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for managing JSON Web Tokens (JWT).
 * This class provides methods to generate and validate JWT tokens,
 * extract claims, and work with token expiration.
 */
@Component
public class JwtTokenUtil {

    private final String secret;
    private final long expirationMs; // Changed to long and renamed for clarity

    /**
     * Constructs a new instance of JwtTokenUtil with the specified secret and expiration values.
     *
     * @param secret the secret key used to sign the JWT tokens (Base64 encoded)
     * @param expirationMs the expiration time for the JWT tokens in milliseconds
     */
    public JwtTokenUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    /**
     * Generates a JSON Web Token (JWT) for the specified user.
     * The token includes claims for the user's authorities and other standard token details,
     * such as subject, issue date, and expiration date.
     *
     * @param userDetails the user details containing information about the user, such as username and authorities
     * @return a JWT as a string containing encoded information about the user
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Add authorities to claims
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Generates a JSON Web Token (JWT) for a given username (for OAuth2 scenarios).
     * The token is built using the provided username and includes claims,
     * an issue time, and an expiration time.
     *
     * @param username the username for which the token is to be generated
     * @return a JWT as a string
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Creates a JWT token with the specified claims and subject.
     *
     * @param claims additional claims to include in the token
     * @param subject the subject (username) of the token
     * @return the generated JWT token as a string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expirationDate = new Date(nowMillis + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token the JWT token from which the username will be extracted
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a given JWT token.
     *
     * @param token the JWT token from which the expiration date is to be extracted
     * @return the expiration date of the token as a {@link Date} object
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token using a provided claim resolver function.
     *
     * @param <T> the type of the claim to be extracted
     * @param token the JWT token from which the claim needs to be extracted
     * @param claimsResolver a function to resolve and retrieve a specific claim from the parsed claims
     * @return the extracted claim of type T
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a given JWT token.
     *
     * @param token the JWT token from which claims are to be extracted
     * @return the {@code Claims} object containing all claims present in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates a JSON Web Token (JWT) against user details by checking if the username
     * in the token matches the provided user details and if the token is not expired.
     *
     * @param token the JWT to validate
     * @param userDetails the user details to validate the token against
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the given JWT token is expired by comparing its expiration date
     * with the current date.
     *
     * @param token the JWT token to be checked for expiration
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Retrieves and constructs the signing key used for JWT creation and validation.
     * The key is decoded from a Base64-encoded secret string and transformed into a secure HMAC (Hash-based Message Authentication Code)
     * SHA key suitable for the specified token signature algorithm.
     *
     * @return the signing key used for creating and validating JWT tokens
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
