package lk.ashan.routenetlkserverapllication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APIErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Handles authorization failures (HTTP 403 Forbidden).
 *
 * Invoked when an authenticated user attempts to access a resource
 * without the required authority or role.
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        log.warn("Access denied. URI: {}, User: {}, Reason: {}",
                request.getRequestURI(),
                request.getUserPrincipal() != null
                        ? request.getUserPrincipal().getName()
                        : "Anonymous",
                accessDeniedException.getMessage());

        ResponseEntity<APIErrorResponse> errorResponse =
                APIResponseBuilder.error(
                        ErrorCode.ACCESS_DENIED,
                        List.of("You do not have permission to perform this operation."),
                        request
                );

        response.setStatus(errorResponse.getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writeValue(response.getOutputStream(), errorResponse.getBody());
    }
}
