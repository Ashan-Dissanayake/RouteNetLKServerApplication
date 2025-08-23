package lk.ashan.routenetlkserverapllication.shared.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APISuccessResponse<T> {

    private T data;
    private Map<String, Object> meta;
    private Map<String, String> links;

}
