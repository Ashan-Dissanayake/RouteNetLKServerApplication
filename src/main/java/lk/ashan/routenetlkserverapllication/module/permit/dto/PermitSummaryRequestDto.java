package lk.ashan.routenetlkserverapllication.module.permit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PermitSummaryRequestDto {
    private Integer id;
    private String number;
    private RouteSummaryRequestDto route;
}
