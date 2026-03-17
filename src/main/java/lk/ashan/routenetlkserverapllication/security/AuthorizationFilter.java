package lk.ashan.routenetlkserverapllication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APIErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AuthorizationFilter is a Spring Security filter that intercepts incoming HTTP requests
 * to verify the presence and validity of a JWT token in the Authorization header.
 * It ensures that only authenticated users with a valid token are provided access.
 *
 * The class extends OncePerRequestFilter, ensuring that the filter is executed
 * once per request within a single request lifecycle. It integrates with Spring's
 * SecurityContextHolder to manage authentication.
 *
 * Responsibilities:
 * - Extract and verify the JWT token from the incoming request's Authorization header.
 * - Extract the username from the token and validate the token using JwtTokenUtil.
 * - Authenticate the user by setting the authentication details in the SecurityContext.
 * - If no valid token is provided, the filter delegates the request further in the filter chain.
 *
 * Dependencies:
 * - JwtTokenUtil: Utility for extracting, validating, and managing JWT tokens.
 * - UserDetailsService: Service to load user-specific data.
 *
 * Methods:
 * - doFilterInternal: Core method to process the filter logic, including token validation and user authentication.
 */
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userService;

    /**
     * Filters incoming HTTP requests to validate JWT-based authorization.
     * Extracts and validates the JWT from the `Authorization` header.
     * If the JWT token is valid, it establishes the user's authentication in the security context.
     * If no valid token is found, it simply continues the filter chain without altering authentication.
     *
     * @param request the HTTP request object containing client request information
     * @param response the HTTP response object for sending responses to the client
     * @param chain the filter chain for processing the request
     * @throws IOException in case of I/O errors during request processing
     * @throws ServletException in case of any servlet-related errors during request processing
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain
    ) throws IOException, ServletException {

        // Check Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        // If no Authorization header or doesn't start with "Bearer ", continue without authentication
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract token from the Authorization header
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {
            // Extract username from token
            String username = jwtTokenUtil.extractUsername(token);

            // If username is valid and no authentication exists in context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load UserDetails
                UserDetails userDetails = userService.loadUserByUsername(username);

                // Validate the token
                if (jwtTokenUtil.validateToken(token, userDetails)) {

                    // Log successful token validation
                    System.out.println("JWT token validated successfully for user: " + username);

                    // Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Set authentication details from request
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Set in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            // Continue with the filter chain
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // JWT token has expired
            handleAuthorizationError(
                    request,
                    response,
                    ErrorCode.VALIDATION_FAILED,
                    "JWT token has expired",
                    e.getMessage()
            );

        } catch (MalformedJwtException e) {
            // JWT token is malformed
            handleAuthorizationError(
                    request,
                    response,
                    ErrorCode.VALIDATION_FAILED,
                    "Invalid JWT token format",
                    e.getMessage()
            );

        } catch (SignatureException e) {
            // JWT signature validation failed
            handleAuthorizationError(
                    request,
                    response,
                    ErrorCode.VALIDATION_FAILED,
                    "JWT signature validation failed",
                    e.getMessage()
            );

        } catch (UsernameNotFoundException e) {
            // User not found in database
            handleAuthorizationError(
                    request,
                    response,
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "User not found",
                    e.getMessage()
            );

        } catch (Exception e) {
            // Any other error
            System.err.println("JWT validation error: " + e.getMessage());
            handleAuthorizationError(
                    request,
                    response,
                    ErrorCode.INTERNAL_ERROR,
                    "Error processing JWT token",
                    e.getMessage()
            );
        }
    }

    /**
     * Handles authorization errors by sending a properly formatted error response
     * using the APIResponseBuilder.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param errorCode the error code to use
     * @param primaryMessage the primary error message
     * @param detailMessage additional detail message
     * @throws IOException if writing response fails
     */
    private void handleAuthorizationError(
            HttpServletRequest request,
            HttpServletResponse response,
            ErrorCode errorCode,
            String primaryMessage,
            String detailMessage) throws IOException {

        // Build details list
        List<String> details = new ArrayList<>();
        details.add(primaryMessage);
        if (detailMessage != null && !detailMessage.isEmpty()) {
            details.add(detailMessage);
        }

        // Build error response using APIResponseBuilder
        ResponseEntity<APIErrorResponse> errorResponse =
                APIResponseBuilder.error(errorCode, details, request);

        // Set response status and content type
        response.setStatus(errorResponse.getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write response body
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writeValue(response.getOutputStream(), errorResponse.getBody());
    }
}
