package lk.ashan.routenetlkserverapllication.shared.api;

import jakarta.servlet.http.HttpServletRequest;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APIErrorResponse;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Map;

public class APIResponseBuilder {

    // ==================== FLEXIBLE CORE METHODS ====================

    /**
     * Most flexible method - specify everything
     * Use when you need custom status or metadata
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> success(
            T data,
            HttpStatus status,
            Map<String, Object> meta,
            Map<String, String> links) {

        return new ResponseEntity<>(
                new APISuccessResponse<>(data, meta, links),
                status
        );
    }

    /**
     * Simple success with just data (200 OK)
     * Most common use case
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> ok(T data) {
        return new ResponseEntity<>(
                new APISuccessResponse<>(
                        data,
                        Map.of(),
                        Map.of("self", buildCurrentRequestUrl())
                ),
                HttpStatus.OK
        );
    }

    /**
     * Success with custom metadata (200 OK)
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> ok(T data, Map<String, Object> meta) {
        return new ResponseEntity<>(
                new APISuccessResponse<>(
                        data,
                        meta,
                        Map.of("self", buildCurrentRequestUrl())
                ),
                HttpStatus.OK
        );
    }

    // ==================== CONVENIENCE METHODS ====================

    /**
     * For resource creation (201 CREATED)
     * Use only when actually creating a new resource
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> created(T data, Object id) {
        return new ResponseEntity<>(
                new APISuccessResponse<>(
                        data,
                        Map.of("status", "created"),
                        Map.of("self", buildUrlWithPath("/" + id))
                ),
                HttpStatus.CREATED
        );
    }

    /**
     * For resource updates (200 OK)
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> updated(T data, Object id) {
        return new ResponseEntity<>(
                new APISuccessResponse<>(
                        data,
                        Map.of("status", "updated"),
                        Map.of("self", buildUrlWithPath("/" + id))
                ),
                HttpStatus.OK
        );
    }

    /**
     * For async/background operations (202 ACCEPTED)
     * Returns immediately while processing continues
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> accepted(T data) {
        return new ResponseEntity<>(
                new APISuccessResponse<>(
                        data,
                        Map.of("status", "processing"),
                        Map.of("self", buildCurrentRequestUrl())
                ),
                HttpStatus.ACCEPTED
        );
    }

    /**
     * For successful deletion (204 NO CONTENT)
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> deleted(Object id) {
        return new ResponseEntity<>(
                new APISuccessResponse<>(
                        null,
                        Map.of("status", "deleted"),
                        Map.of("self", buildUrlWithPath("/" + id))
                ),
                HttpStatus.NO_CONTENT
        );
    }

    /**
     * For list/collection responses (200 OK)
     * Includes count in metadata
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> list(T data, int count) {
        return new ResponseEntity<>(
                new APISuccessResponse<>(
                        data,
                        Map.of("count", count),
                        Map.of("self", buildCurrentRequestUrl())
                ),
                HttpStatus.OK
        );
    }

    /**
     * For paginated responses (200 OK)
     * Includes pagination metadata and links
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> paginated(
            T data,
            int page,
            int size,
            long totalElements,
            int totalPages) {

        Map<String, Object> meta = Map.of(
                "page", page,
                "size", size,
                "totalElements", totalElements,
                "totalPages", totalPages
        );

        Map<String, String> links = Map.of(
                "self", buildCurrentRequestUrl(),
                "first", buildUrlWithParams("page=0&size=" + size),
                "last", buildUrlWithParams("page=" + (totalPages - 1) + "&size=" + size)
        );

        return new ResponseEntity<>(
                new APISuccessResponse<>(data, meta, links),
                HttpStatus.OK
        );
    }

    /**
     * For operations that return no content (204 NO CONTENT)
     */
    public static <T> ResponseEntity<APISuccessResponse<T>> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ==================== BACKWARD COMPATIBLE (DEPRECATED) ====================

    /**
     * @deprecated Use created() or ok() depending on actual operation
     * This method assumes all POST = 201 CREATED, which isn't always true
     */
    @Deprecated
    public static <T> ResponseEntity<APISuccessResponse<T>> postResponse(T data, Object id) {
        return created(data, id);
    }

    /**
     * @deprecated Use updated() instead
     */
    @Deprecated
    public static <T> ResponseEntity<APISuccessResponse<T>> putResponse(T data, Object id) {
        return updated(data, id);
    }

    /**
     * @deprecated Use list() instead
     */
    @Deprecated
    public static <T> ResponseEntity<APISuccessResponse<T>> getResponse(T data, int count) {
        return list(data, count);
    }

    /**
     * @deprecated Use deleted() instead
     */
    @Deprecated
    public static <T> ResponseEntity<APISuccessResponse<T>> deleteResponse(Object id) {
        return deleted(id);
    }

    // ==================== ERROR RESPONSE ====================

    /**
     * Build RFC 7807 compliant error response
     */
    public static ResponseEntity<APIErrorResponse> error(
            ErrorCode errorCode,
            List<String> details,
            HttpServletRequest request) {

        String instanceUri = buildInstanceUrl(request);

        return new ResponseEntity<>(new APIErrorResponse(
                "https://localhost" + errorCode.getTypeUri(),
                errorCode.getTitle(),
                errorCode.getStatus(),
                errorCode,
                details,
                instanceUri
        ), errorCode.getStatus());
    }

    // ==================== URL BUILDERS ====================

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

    private static String buildUrlWithParams(String queryParams) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replaceQuery(queryParams)
                .toUriString();
    }

    private static String buildInstanceUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder
                .fromRequestUri(request)
                .toUriString();
    }
}
