package lk.ashan.ntcserverapllication.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIErrorResponse {

    private String type;           // URI of the error
    private String title;          // Human-readable error title
    private HttpStatus status;     // HTTP status
    private ErrorCode code;        // Enum error code
    private String detail;         // Detailed error message
    private String instance;       // URI of the request
}
