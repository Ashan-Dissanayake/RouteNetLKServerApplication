package lk.ashan.ntcserverapllication.shared;

import jakarta.servlet.http.HttpServletRequest;
import lk.ashan.ntcserverapllication.paylaod.response.APIErrorResponse;
import lk.ashan.ntcserverapllication.paylaod.response.APISuccessResponse;
import lk.ashan.ntcserverapllication.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

public class APIResponseBuilder {

    public static <T> ResponseEntity<APISuccessResponse<T>> getResponse(T data, int count) {
        return ResponseEntity.ok(new APISuccessResponse<>(
                data,
                Map.of("count", count),
                Map.of("self", buildCurrentRequestUrl())
        ));
    }

    public static <T> ResponseEntity<APISuccessResponse<T>> postResponse(T data, Object id) {
        return new ResponseEntity<>(new APISuccessResponse<>(
                data,
                Map.of("status", "created"),
                Map.of("self", buildUrlWithPath("/" + id))
        ), HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<APISuccessResponse<T>> putResponse(T data, Object id) {
        return new ResponseEntity<>(new APISuccessResponse<>(
                data,
                Map.of("status", "updated"),
                Map.of("self", buildUrlWithPath("/"+id))
        ), HttpStatus.OK);
    }

    public static <T> ResponseEntity<APISuccessResponse<T>> deleteResponse(Object id) {
        return new ResponseEntity<>(new APISuccessResponse<>(
                null,
                Map.of("status", "deleted"),
                Map.of("self", buildUrlWithPath("/"+id))
        ), HttpStatus.NO_CONTENT);
    }

    public static ResponseEntity<APIErrorResponse> error(
            ErrorCode errorCode,
            String detail,
            HttpServletRequest request
    ) {
        String instanceUri = buildInstanceUrl(request);
        return new ResponseEntity<>(new APIErrorResponse(
                "https://localhost" + errorCode.getTypeUri(),
                errorCode.getTitle(),
                errorCode.getStatus(),
                errorCode,
                detail,
                instanceUri
        ), errorCode.getStatus());
    }

    private static String buildCurrentRequestUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .toUriString();
    }

    private static String buildUrlWithPath(String pathSegment) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(pathSegment)
                .toUriString();
    }

    private static String buildInstanceUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder
                .fromRequestUri(request)
                .toUriString();
    }
}

