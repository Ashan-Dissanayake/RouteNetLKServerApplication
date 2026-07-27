package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryResponseDto;
import lombok.*;

/**
 * A Data Transfer Object (DTO) representing a summary of a trip.
 * This class includes details such as the trip ID and the associated permit summary.
 * It is annotated with Lombok annotations for boilerplate code reduction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripSummaryResponseDto {
    private Integer id;
    private PermitSummaryResponseDto permit;
}
