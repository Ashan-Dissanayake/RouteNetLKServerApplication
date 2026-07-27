package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) representing a summary of an operational calendar.
 * This class is used to encapsulate the data for transferring between layers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OpCalenderSummaryDto {
    private Integer id;
    private String name;
}
