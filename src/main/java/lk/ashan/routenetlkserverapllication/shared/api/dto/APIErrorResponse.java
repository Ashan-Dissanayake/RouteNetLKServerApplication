package lk.ashan.routenetlkserverapllication.shared.api.dto;

import lk.ashan.routenetlkserverapllication.shared.api.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIErrorResponse {

    private String type;           // URI of the error
    private String title;          // Human-readable error title
    private HttpStatus status;     // HTTP status
    private ErrorCode code;        // Enum error code
    private List<String> details;   // for multiple error details
    private String instance;       // URI of the request
}
