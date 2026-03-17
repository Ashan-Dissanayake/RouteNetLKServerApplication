package lk.ashan.routenetlkserverapllication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APIErrorResponse;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        // Set the login URL
        setFilterProcessesUrl("/login");

        try {
            // Read login request
            LoginRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            // Log the attempt
            System.out.println("Login attempt for username: " + loginRequest.getUsername());

            // Create authentication token
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );

            // Authenticate
            return authenticationManager.authenticate(authRequest);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        // Get authenticated user
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(userDetails);

        // Log success
        System.out.println("Login successful for: " + userDetails.getUsername());

        // Prepare response data
        Map<String, Object> responseData = Map.of(
                "username", userDetails.getUsername(),
                "token", token,
                "authorities", userDetails.getAuthorities()
        );

        // Build success response using APIResponseBuilder
        ResponseEntity<APISuccessResponse<Map<String, Object>>> successResponse =
                APIResponseBuilder.ok(
                        responseData,
                        Map.of("status", "authenticated")
                );

        // Add token to header
        response.addHeader("Authorization", "Bearer " + token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        // Set response status and content type
        response.setStatus(successResponse.getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write response body
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writeValue(response.getOutputStream(), successResponse.getBody());
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        // Log the failure
        System.out.println("Authentication failed: " + failed.getMessage());

        // Determine the appropriate ErrorCode based on exception type
        ErrorCode errorCode;
        List<String> details = new ArrayList<>();

        if (failed instanceof BadCredentialsException) {
            errorCode = ErrorCode.VALIDATION_FAILED;
            details.add("Invalid username or password");
            details.add(failed.getMessage());
        } else if (failed instanceof LockedException) {
            errorCode = ErrorCode.BUSINESS_RULE_VIOLATION;
            details.add("Account is locked");
            details.add(failed.getMessage());
        } else if (failed instanceof UsernameNotFoundException) {
            errorCode = ErrorCode.RESOURCE_NOT_FOUND;
            details.add("User not found");
            details.add(failed.getMessage());
        } else if (failed instanceof DisabledException) {
            errorCode = ErrorCode.BUSINESS_RULE_VIOLATION;
            details.add("Account is disabled");
            details.add(failed.getMessage());
        } else {
            errorCode = ErrorCode.INTERNAL_ERROR;
            details.add("Authentication failed");
            details.add(failed.getMessage());
        }

        // Build error response using your APIResponseBuilder
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
