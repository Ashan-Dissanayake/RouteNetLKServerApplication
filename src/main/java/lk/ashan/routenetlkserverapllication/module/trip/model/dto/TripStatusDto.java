package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing the status of a trip.
 * This class is used to transfer trip status data between different layers of the application.
 * It includes fields for the unique identifier and the name of the trip status.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripStatusDto {
    private Integer id;
    private String name;
}
