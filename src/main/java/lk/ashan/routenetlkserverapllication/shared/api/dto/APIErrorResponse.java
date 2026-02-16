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
    private String type;
    private String title;
    private HttpStatus status;
    private ErrorCode code;
    private List<String> details;
    private String instance;
}
