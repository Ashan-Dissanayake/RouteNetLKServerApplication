package lk.ashan.routenetlkserverapllication.module.permit.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PermitSummaryResponseDto {
    private Integer id;
    private String number;
}
