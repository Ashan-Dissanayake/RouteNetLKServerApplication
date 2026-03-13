package lk.ashan.routenetlkserverapllication.module.permit.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RouteSummaryRequestDto {
    private Integer id;
    private String number;
    private Integer mingapminutes;
}
