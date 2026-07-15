package lk.ashan.routenetlkserverapllication.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataDTO {

    private List<String> labels;
    private List<DatasetDTO> datasets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatasetDTO {
        private String label;
        private List<?> data;
    }


}
